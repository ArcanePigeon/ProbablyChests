package org.cloudwarp.probablychests.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.screenhandlers.PCScreenHandler;

public class PCChestScreen extends HandledScreen<PCScreenHandler> {
	private static final Identifier TEXTURE = ProbablyChests.id("textures/gui/pc_chest_gui.png");

	public PCChestScreen (PCScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init(){
		super.init();
		titleX = (backgroundWidth - textRenderer.getWidth(title))/2;
	}

	@Override
	protected void drawBackground (MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
		RenderSystem.setShaderTexture(0,TEXTURE);
		int x = (width-backgroundWidth)/2;
		int y = (height-backgroundHeight)/2;
		drawTexture(matrices,x,y,0,0,backgroundWidth,backgroundHeight);
	}

	@Override
	public void render (MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices,mouseX,mouseY);
	}
}
