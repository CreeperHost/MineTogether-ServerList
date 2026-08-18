package net.creeperhost.minetogetherservers.fabric.mixin;

import net.creeperhost.minetogetherservers.gui.MTTextures;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AtlasManager.class)
public class AtlasManagerMixin {

    @Shadow @Final private Map<Identifier, AtlasManager.AtlasEntry> atlasByTexture;
    @Shadow @Final private Map<Identifier, AtlasManager.AtlasEntry> atlasById;

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/texture/TextureManager;I)V", at = @At("TAIL"))
    private void registerMineTogetherAtlas(TextureManager textureManager, int mipLevel, CallbackInfo ci) {
        AtlasManager.AtlasConfig config = new AtlasManager.AtlasConfig(MTTextures.TEXTURE_ID, MTTextures.DEFINITION_LOCATION, false);
        TextureAtlas atlas = new TextureAtlas(config.textureId());
        textureManager.register(config.textureId(), atlas);
        AtlasManager.AtlasEntry entry = new AtlasManager.AtlasEntry(atlas, config);
        atlasByTexture.put(config.textureId(), entry);
        atlasById.put(config.definitionLocation(), entry);
    }
}
