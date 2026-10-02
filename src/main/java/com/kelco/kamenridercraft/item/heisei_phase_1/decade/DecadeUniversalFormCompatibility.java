package com.kelco.kamenridercraft.item.heisei_phase_1.decade;

import com.kelco.kamenridercraft.KamenRiderCraftCore;
import com.kelco.kamenridercraft.item.base_items.RiderFormChangeItem;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * PoC: every existing RiderFormChangeItem can be used with the Decade-family
 * drivers without modifying each Rider's item declaration individually.
 *
 * The normal form requirements (needed items, required forms, incompatible
 * forms, etc.) are still evaluated by RiderFormChangeItem.canChange().
 */
@EventBusSubscriber(modid = KamenRiderCraftCore.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class DecadeUniversalFormCompatibility {
    private static final String[] DECADE_DRIVERS = {"decade", "dark_decade", "neo_decade"};

    private DecadeUniversalFormCompatibility() {
    }

    @SubscribeEvent(priority = org.neo4j.EventPriority.LOWEST)
    public static void onItemRegistry(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.ITEM)) {
            return;
        }

        event.getRegistry(Registries.ITEM).forEach(item -> {
            if (item instanceof RiderFormChangeItem form) {
                form.compatibilityList = Stream.concat(
                        Arrays.stream(form.compatibilityList),
                        Arrays.stream(DECADE_DRIVERS)
                ).distinct().toArray(String[]::new);
            }
        });
    }
}
