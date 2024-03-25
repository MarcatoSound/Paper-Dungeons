package io.papermc.generator;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.papermc.generator.types.goal.MobGoalNames;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public class MobGoalConverterTest {

    @Test
    public void testBukkitMap() {
        List<Class<Mob>> classes;
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages(Entity.class.getPackageName()).scan()) {
            classes = scanResult.getSubclasses(Mob.class.getName()).loadClasses(Mob.class);
        }
        assertFalse(classes.isEmpty(), "There are supposed to be more than 0 mob classes!");

        List<String> missingClasses = new ArrayList<>();
        for (Class<Mob> nmsClass : classes) {
            Class<? extends org.bukkit.entity.Mob> bukkitClass = MobGoalNames.toBukkitClass(nmsClass);
            if (bukkitClass == null) {
                missingClasses.add(nmsClass.getCanonicalName());
            }
        }

        if (!missingClasses.isEmpty()) {
            fail("Missing some entity classes in the bukkit map (MobGoalNames): " + String.join(", ", missingClasses));
        }
    }
}
