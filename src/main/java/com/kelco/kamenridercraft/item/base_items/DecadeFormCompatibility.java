package com.kelco.kamenridercraft.item.base_items;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Proof of concept for Decade's universal form compatibility.
 *
 * This is intentionally centralized instead of editing hundreds of form declarations.
 * Existing canChange() requirements such as required items, required forms and
 * incompatible forms are still evaluated normally.
 */
public final class DecadeFormCompatibility {
    private static final String[] DECADE_DRIVERS = {"decade", "dark_decade", "neo_decade"};

    private DecadeFormCompatibility() {
    }

    public static void apply() {
        for (var item : BuiltInRegistries.ITEM) {
            if (item instanceof RiderFormChangeItem form) {
                form.compatibilityList = Stream.concat(
                        Arrays.stream(form.compatibilityList),
                        Arrays.stream(DECADE_DRIVERS)
                ).distinct().toArray(String[]::new);
            }
        }
    }

    public static String originalRiderName(RiderFormChangeItem form) {
        return form.riderName;
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(DecadeFormCompatibility::apply);
    }
}
