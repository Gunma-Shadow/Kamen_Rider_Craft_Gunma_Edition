package com.kelco.kamenridercraft.item.base_items;

import com.kelco.kamenridercraft.KamenRiderCraftCore;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Arrays;

/**
 * Proof of concept: lets Decade use existing RiderFormChangeItem instances
 * without editing every individual Rider item declaration.
 *
 * This deliberately changes only the compatibility layer. The form's normal
 * requirements (required items, required form slots, incompatible forms,
 * attack-form restrictions, etc.) remain in RiderFormChangeItem.canChange().
 */
@EventBusSubscriber(modid = KamenRiderCraftCore.MOD_ID)
public final class DecadeFormCompatibility {
    private static final String[] DECADE_DRIVERS = {"decade", "dark_decade", "neo_decade"};

    private DecadeFormCompatibility() {
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        prepare(event.getEntity(), event.getItemStack());
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        prepare(event.getEntity(), event.getItemStack());
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        prepare(event.getEntity(), event.getItemStack());
    }

    @SubscribeEvent
    public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        prepare(event.getEntity(), event.getItemStack());
    }

    private static void prepare(net.minecraft.world.entity.player.Player player, ItemStack stack) {
        if (!(stack.getItem() instanceof RiderFormChangeItem form))
            return;

        if (!(player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof RiderDriverItem belt))
            return;

        if (!isDecadeDriver(belt.riderName))
            return;

        // Preserve any form-specific compatibility entries already present.
        if (form.compatibilityList == null)
            form.compatibilityList = DECADE_DRIVERS.clone();
        else {
            form.compatibilityList = Arrays.stream(form.compatibilityList)
                    .filter(value -> value != null && !value.isEmpty())
                    .concat(Arrays.stream(DECADE_DRIVERS))
                    .distinct()
                    .toArray(String[]::new);
        }

        // RiderDriverItem asks the form for its model using the belt rider name.
        // Keep an explicit changeRiderName() override intact; otherwise point it
        // back to the form's original rider so Kuuga, Build, Gotchard, etc. keep
        // their own armor/model resources while worn with Decadriver.
        if (form.overrideRiderName == null)
            form.overrideRiderName = form.riderName;

        // RiderDriverItem normally takes the belt texture from the form. For the
        // Decadriver POC force its own belt texture without touching every form.
        belt.beltText = "decadriver_belt";
    }

    private static boolean isDecadeDriver(String riderName) {
        return Arrays.asList(DECADE_DRIVERS).contains(riderName);
    }
}
