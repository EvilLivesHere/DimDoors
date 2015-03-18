package StevenDimDoors.dimdoors.world.gen;

import StevenDimDoors.dimdoors.block.DDBlocks;
import StevenDimDoors.dimdoors.config.DDProperties;
import StevenDimDoors.dimdoors.ticking.CustomLimboPopulator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;

public class ChunkProviderLimbo extends ChunkProviderGenerate {

    /**
     * RNG.
     */
    private Random rand;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    private NoiseGeneratorOctaves noiseGen4;
    /**
     * A NoiseGeneratorOctaves used in generating terrain
     */
    public NoiseGeneratorOctaves noiseGen5;
    /**
     * A NoiseGeneratorOctaves used in generating terrain
     */
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    /**
     * Reference to the World object.
     */
    private World worldObj;
    /**
     * are map structures going to be generated (e.g. strongholds)
     */
    private WorldType worldType;
    private final double[] noiseArray;
    private final float[] parabolicField;
    private double[] stoneNoise = new double[256];

    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();

    /**
     * The biomes that are used to generate the chunk
     */
    private BiomeGenBase[] biomesForGeneration;
    double[] noise3;
    double[] noise1;
    double[] noise2;
    double[] noise5;
    double[] noise6;
    int[][] field_73219_j = new int[32][32];

    public ChunkProviderLimbo(World world, long seed) {
        super(world, seed, false);
        this.worldObj = world;
        this.worldType = world.getWorldInfo().getTerrainType();
        this.rand = new Random(seed);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16); // Base terrain
        this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16); // Hillyness
        this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 80); // Seems to adjust the size of features, how stretched things are -default 8
        this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseArray = new double[425];
        this.parabolicField = new float[25];

        for (int j = -2; j <= 2; ++j) {
            for (int k = -2; k <= 2; ++k) {
                float f = 10.0F / MathHelper.sqrt_float((float) (j * j + k * k) + 0.2F);
                this.parabolicField[j + 2 + (k + 2) * 5] = f;
            }
        }
    }

    @Override
    public void func_147424_a(int par1, int par2, Block[] par3ArrayOfBlock) { // function: generateTerrain
        byte b0 = 4;
        byte b1 = 16;
        byte b2 = 19; // Default was 63
        int k = b0 + 1;
        byte b3 = 17;
        int l = b0 + 1;
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, par1 * 4 - 2, par2 * 4 - 2, k + 5, l + 5);
        this.func_147423_a(par1 * 4, 0, par2 * 4);

        for (int i1 = 0; i1 < b0; ++i1) {
            for (int j1 = 0; j1 < b0; ++j1) {
                for (int k1 = 0; k1 < b1; ++k1) {
                    double d0 = 0.125D;
                    double d1 = this.noiseArray[((i1 + 0) * l + j1 + 0) * b3 + k1 + 0];
                    double d2 = this.noiseArray[((i1 + 0) * l + j1 + 1) * b3 + k1 + 0];
                    double d3 = this.noiseArray[((i1 + 1) * l + j1 + 0) * b3 + k1 + 0];
                    double d4 = this.noiseArray[((i1 + 1) * l + j1 + 1) * b3 + k1 + 0];
                    double d5 = (this.noiseArray[((i1 + 0) * l + j1 + 0) * b3 + k1 + 1] - d1) * d0;
                    double d6 = (this.noiseArray[((i1 + 0) * l + j1 + 1) * b3 + k1 + 1] - d2) * d0;
                    double d7 = (this.noiseArray[((i1 + 1) * l + j1 + 0) * b3 + k1 + 1] - d3) * d0;
                    double d8 = (this.noiseArray[((i1 + 1) * l + j1 + 1) * b3 + k1 + 1] - d4) * d0;

                    for (int l1 = 0; l1 < 8; ++l1) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i2 = 0; i2 < 4; ++i2) {
                            int j2 = i2 + i1 * 4 << 11 | 0 + j1 * 4 << 7 | k1 * 8 + l1;
                            short short1 = 128;
                            j2 -= short1;
                            double d14 = 0.25D;
                            double d15 = (d11 - d10) * d14;
                            double d16 = d10 - d15;

                            for (int k2 = 0; k2 < 4; ++k2) {
                                if ((d16 += d15) > 0.0D) {
                                    par3ArrayOfBlock[j2 += short1] = DDBlocks.blockLimbo;
                                } else if (k1 * 8 + l1 < b2) {
                                    par3ArrayOfBlock[j2 += short1] = DDBlocks.blockDimWallPerm;
                                } else {
                                    par3ArrayOfBlock[j2 += short1] = null;
                                }
                            }
                            d10 += d12;
                            d11 += d13;
                        }
                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    @Override
    public void replaceBlocksForBiome(int p_147422_1_, int p_147422_2_, Block[] p_147422_3_, byte[] p_147422_4_, BiomeGenBase[] p_147422_5_) {
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    @Override
    public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        return this.provideChunk(p_73158_1_, p_73158_2_);
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the specified chunk from the map seed and
     * chunk seed
     */
    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        this.rand.setSeed((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L);
        Block[] ablock = new Block[32768];
        this.func_147424_a(chunkX, chunkZ, ablock);
        Chunk chunk = new Chunk(this.worldObj, ablock, chunkX, chunkZ);
        chunk.generateSkylightMap();

        if (!chunk.isTerrainPopulated) {
            chunk.isTerrainPopulated = true;
            CustomLimboPopulator.registerChunkForPopulation(DDProperties.instance().LimboDimensionID, chunkX, chunkZ);
        }
        return chunk;
    }

    private void func_147423_a(int par2, int par3, int par4) {
        int par6 = 17;  // Was default 33
        double d0 = 884.412D; //large values here create spiky land. add a 0, good -default 684.412
        double d1 = 9840.412D; //large values here make sheets- default - 684.412
        //this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, par2, par4, 5, 5, 1.121D, 1.121D, 0.5D); // This doesn't seem used
        this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, par2, par4, 5, 5, 200.0D, 200.0D, 0.5D);
        this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, par2, par3, par4, 5, par6, 5, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
        this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, par2, par3, par4, 5, par6, 5, d0, d1, d0);
        this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, par2, par3, par4, 5, par6, 5, d0, d1, d0);
        int l = 0;
        int i1 = 0;
        double d4 = 8.5D;

        for (int j1 = 0; j1 < 5; ++j1) {
            for (int k1 = 0; k1 < 5; ++k1) {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                byte b0 = 2;

                for (int l1 = -b0; l1 <= b0; ++l1) {
                    for (int i2 = -b0; i2 <= b0; ++i2) {
                        BiomeGenBase biome = BiomeGenBase.plains;
                        float f3 = biome.rootHeight;
                        float f4 = biome.heightVariation;
                        float f5 = this.parabolicField[l1 + 2 + (i2 + 2) * 5] / (f3 + 9.0F);

                        f += (f4 * f5) + 4;
                        f1 += (f3 * f5) - 1;
                        f2 += f5;
                    }
                }

                f /= f2;
                f1 /= f2;
                f = f * 0.9F + 0.1F;
                f1 = (f1 * 4.0F - 1.0F) / 8.0F;
                double d12 = this.noise6[i1] / 8000.0D;

                if (d12 < 0.0D) {
                    d12 = -d12 * 0.3D;
                }

                d12 = d12 * 3.0D - 2.0D;

                if (d12 < 0.0D) {
                    d12 /= 2.0D;

                    if (d12 < -1.0D) {
                        d12 = -1.0D;
                    }

                    d12 /= 1.4D;
                    d12 /= 2.0D;
                } else {
                    if (d12 > 1.0D) {
                        d12 = 1.0D;
                    }

                    d12 /= 8.0D;
                }

                ++i1;

                double d13 = (double) f1;
                double d14 = (double) f;
                d13 += d12 * 0.2D;
                d13 = d13 * par6 / 16.0D;
                double d5 = par6 / 2.0D + d13 * 4.0D;
                double d10 = 0.0D;

                for (int j2 = 0; j2 < par6; ++j2) {
                    double d6 = ((double) j2 - d5) * 12.0D / d14;

                    if (d6 < 0.0D) {
                        d6 *= 4.0D;
                    }

                    double d7 = this.noise1[l] / 512.0D;
                    double d8 = this.noise2[l] / 512.0D;
                    double d9 = (this.noise3[l] / 10.0D + 1.0D) / 2.0D;

                    if (d9 < 0.0D) {
                        d10 = d7;
                    } else if (d9 > 1.0D) {
                        d10 = d8;
                    } else {
                        d10 = d7 + (d8 - d7) * d9;
                    }

                    d10 -= d6;

                    if (j2 > par6 - 4) {
                        double d11 = (double) ((float) (j2 - (par6 - 4)) / 3.0F);
                        d10 = d10 * (1.0D - d11) + -10.0D * d11;
                    }

                    this.noiseArray[l] = d10;
                    ++l;
                }
            }
        }
    }

    /**
     * Populates chunk with ores etc etc
     */
    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    @Override
    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int x, int y, int z) {
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(x, z);
        return par1EnumCreatureType == EnumCreatureType.monster && this.scatteredFeatureGenerator.func_143030_a(x, y, z) ? this.scatteredFeatureGenerator.getScatteredFeatureSpawnList() : biomegenbase.getSpawnableList(par1EnumCreatureType);
    }

    @Override
    public ChunkPosition func_147416_a(World var1, String var2, int var3, int var4, int var5) { // function:findClosestStructure
        return null;
    }

    @Override
    public void recreateStructures(int var1, int var2) {
    }
}
