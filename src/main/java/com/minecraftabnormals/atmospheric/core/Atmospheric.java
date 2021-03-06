package com.minecraftabnormals.atmospheric.core;

import com.minecraftabnormals.atmospheric.core.other.AtmosphericCompat;
import com.minecraftabnormals.atmospheric.core.other.AtmosphericRender;
import com.minecraftabnormals.atmospheric.core.other.AtmosphericVillagers;
import com.minecraftabnormals.atmospheric.core.registry.AtmosphericBiomes;
import com.minecraftabnormals.atmospheric.core.registry.AtmosphericEffects;
import com.minecraftabnormals.atmospheric.core.registry.AtmosphericFeatures;
import com.minecraftabnormals.atmospheric.core.registry.AtmosphericParticles;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Atmospheric.MODID)
@Mod.EventBusSubscriber(modid = Atmospheric.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Atmospheric {
	public static final String MODID = "atmospheric";
	public static final AtmosphericRegistryHelper REGISTRY_HELPER = new AtmosphericRegistryHelper(MODID);

	public Atmospheric() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		REGISTRY_HELPER.getDeferredItemRegister().register(bus);
		REGISTRY_HELPER.getDeferredBlockRegister().register(bus);
		REGISTRY_HELPER.getDeferredEntityRegister().register(bus);

		AtmosphericBiomes.BIOMES.register(bus);
		AtmosphericFeatures.FEATURES.register(bus);
		AtmosphericParticles.PARTICLES.register(bus);
		AtmosphericEffects.EFFECTS.register(bus);
		AtmosphericEffects.POTIONS.register(bus);

		MinecraftForge.EVENT_BUS.register(this);

		bus.addListener(this::setup);
		bus.addListener(this::clientSetup);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AtmosphericConfig.COMMON_SPEC);
	}

	private void setup(final FMLCommonSetupEvent event) {
		DeferredWorkQueue.runLater(() -> {
			AtmosphericFeatures.generateFeatures();
			AtmosphericFeatures.addCarvables();
			AtmosphericBiomes.addBiomeTypes();
			AtmosphericBiomes.registerBiomesToDictionary();
			AtmosphericVillagers.setupVillagerTypes();
			AtmosphericCompat.registerCompostables();
			AtmosphericCompat.registerFlammables();
			AtmosphericCompat.registerDispenserBehaviors();
			AtmosphericCompat.registerLootInjectors();
			AtmosphericEffects.registerBrewingRecipes();
		});
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		DeferredWorkQueue.runLater(() -> {
			AtmosphericRender.registerBlockColors();
			AtmosphericRender.registerRenderLayers();
			AtmosphericRender.registerEntityRenderers();
		});
	}
}
