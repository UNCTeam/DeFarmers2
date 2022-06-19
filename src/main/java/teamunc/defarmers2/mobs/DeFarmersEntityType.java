package teamunc.defarmers2.mobs;

public enum DeFarmersEntityType {

    ZOMBIE("Zombie"),
    SKELETON("Skeleton"),
    SPIDER("Spider"),
    CAVE_SPIDER("CaveSpider"),
    CREEPER("Creeper"),
    ENDERMAN("Enderman"),
    GHAST("Ghast"),
    PIG_ZOMBIE("PigZombie"),
    SLIME("Slime"),
    WITCH("Witch"),
    WOLF("Wolf"),
    BLAZE("Blaze"),
    CAVE_SPIDER_JOCKEY("CaveSpiderJockey"),
    ENDERMITE("Endermite"),
    MAGMA_CUBE("MagmaCube"),

    ;


    private String name;
    DeFarmersEntityType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
