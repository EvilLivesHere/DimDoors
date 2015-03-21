package StevenDimDoors.dimdoors.item;

import StevenDimDoors.dimdoors.item.base.DDItem;
import static StevenDimDoors.dimdoors.Utilities.modAsset;
import StevenDimDoors.dimdoors.block.BaseDimDoor;
import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.core.DimLink;
import StevenDimDoors.dimdoors.core.LinkType;
import StevenDimDoors.dimdoors.core.NewDimData;
import StevenDimDoors.dimdoors.core.PocketManager;
import StevenDimDoors.dimdoors.helpers.Point4DOrientation;
import StevenDimDoors.dimdoors.mod_pocketDim;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ItemRiftSignature extends DDItem {

    public ItemRiftSignature() {
        this("itemLinkSignature");
    }

    public ItemRiftSignature(String name) {
        super(name);
        this.setMaxDamage(0);
        this.hasSubtypes = true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack, int pass) {
        // Make the item glow if it has one endpoint stored
        return (stack.getItemDamage() != 0);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
	// We must use onItemUseFirst() instead of onItemUse() because Minecraft checks
        // whether the user is in creative mode after calling onItemUse() and undoes any
        // damage we might set to indicate the rift sig has been activated. Otherwise,
        // we would need to rely on checking NBT tags for hasEffect() and that function
        // gets called constantly. Avoiding NBT lookups reduces our performance impact.

        // Return false on the client side to pass this request to the server
        if (world.isRemote) {
            return false;
        }

        //Increase y by 2 to place the rift at head level
        int adjustedY = adjustYForSpecialBlocks(world, x, y + 2, z);
        if (!player.canPlayerEdit(x, adjustedY, z, side, stack)) {
            return true;
        }
        Point4DOrientation source = getSource(stack);
        int orientation = MathHelper.floor_double(((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
        if (source != null) {
            // The link was used before and already has an endpoint stored.
            // Create links connecting the two endpoints.
            NewDimData sourceDimension = PocketManager.getDimensionData(source.getDimension());
            NewDimData destinationDimension = PocketManager.getDimensionData(world);

            DimLink link = sourceDimension.createLink(source.getX(), source.getY(), source.getZ(), LinkType.NORMAL, source.getOrientation());
            DimLink reverse = destinationDimension.createLink(x, adjustedY, z, LinkType.NORMAL, orientation);

            destinationDimension.setLinkDestination(link, x, adjustedY, z);
            sourceDimension.setLinkDestination(reverse, source.getX(), source.getY(), source.getZ());

            // Try placing a rift at the destination point
            DDBlocks.blockRift.tryPlacingRift(world, x, adjustedY, z);

            // Try placing a rift at the source point
            // We don't need to check if sourceWorld is null - that's already handled.
            World sourceWorld = DimensionManager.getWorld(sourceDimension.id());
            DDBlocks.blockRift.tryPlacingRift(sourceWorld, source.getX(), source.getY(), source.getZ());

            if (!player.capabilities.isCreativeMode) {
                stack.stackSize--;
            }
            clearSource(stack);
            mod_pocketDim.sendChat(player, "Rift Created");
            world.playSoundAtEntity(player, modAsset("riftEnd"), 0.6f, 1);
        } else {
            //The link signature has not been used. Store its current target as the first location.
            setSource(stack, x, adjustedY, z, orientation, PocketManager.createDimensionData(world));
            mod_pocketDim.sendChat(player, ("Location Stored in Rift Signature"));
            world.playSoundAtEntity(player, modAsset("riftStart"), 0.6f, 1);
        }
        return true;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        Point4DOrientation source = getSource(par1ItemStack);
        if (source != null) {
            par3List.add("Leads to (" + source.getX() + ", " + source.getY() + ", " + source.getZ() + ") at dimension #" + source.getDimension());
        } else {
            par3List.add("First click stores a location;");
            par3List.add("second click creates a pair of");
            par3List.add("rifts linking the two locations.");
        }
    }

    /**
     * Makes the rift placement account for replaceable blocks and doors.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @return the adjusted y coord
     */
    public static int adjustYForSpecialBlocks(World world, int x, int y, int z) {
        int targetY = y - 2; // Get the block the player actually clicked on
        Block block = world.getBlock(x, targetY, z);
        if (block == null) {
            return targetY + 2;
        }
        if (block.isReplaceable(world, x, targetY, z)) {
            return targetY + 1; // Move block placement down (-2+1) one so its directly over things like snow
        }
        if (block instanceof BaseDimDoor) {
            if (((BaseDimDoor) block).isUpperDoorBlock(world.getBlockMetadata(x, targetY, z))) {
                return targetY; // Move rift placement down two so its in the right place on the door.
            }
            // Move rift placement down one so its in the right place on the door.
            return targetY + 1;
        }
        return targetY + 2;
    }

    public static void setSource(ItemStack itemStack, int x, int y, int z, int orientation, NewDimData dimension) {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("linkX", x);
        tag.setInteger("linkY", y);
        tag.setInteger("linkZ", z);
        tag.setInteger("orientation", orientation);
        tag.setInteger("linkDimID", dimension.id());

        itemStack.setTagCompound(tag);
        itemStack.setItemDamage(1);
    }

    public static void clearSource(ItemStack itemStack) {
        //Don't just set the tag to null since there may be other data there (e.g. for renamed items)
        NBTTagCompound tag = itemStack.getTagCompound();
        tag.removeTag("linkX");
        tag.removeTag("linkY");
        tag.removeTag("linkZ");
        tag.removeTag("orientation");
        tag.removeTag("linkDimID");
        itemStack.setItemDamage(0);
    }

    public static Point4DOrientation getSource(ItemStack itemStack) {
        if (itemStack.getItemDamage() != 0) {
            if (itemStack.hasTagCompound()) {
                NBTTagCompound tag = itemStack.getTagCompound();

                int x = tag.getInteger("linkX");
                int y = tag.getInteger("linkY");
                int z = tag.getInteger("linkZ");
                int orientation = tag.getInteger("orientation");
                int dimID = tag.getInteger("linkDimID");

                return new Point4DOrientation(x, y, z, orientation, dimID);

            }
            // Mark the item as uninitialized if its source couldn't be read
            itemStack.setItemDamage(0);
        }
        return null;
    }
}
