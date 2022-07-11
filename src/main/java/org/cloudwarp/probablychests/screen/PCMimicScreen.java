package org.cloudwarp.probablychests.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.entity.PCTameablePetWithInventory;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;

public class PCMimicScreen extends HandledScreen<PCMimicScreenHandler> {
	private static final Identifier TEXTURE = ProbablyChests.id("textures/gui/pc_mimic_gui.png");
	private final int rows;

	public PCMimicScreen (PCMimicScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.passEvents = false;
		this.rows = handler.getRows();
		this.backgroundHeight = 114 + this.rows * 18;
		this.playerInventoryTitleY = this.backgroundHeight - 94;
	}

	@Override
	protected void init(){
		super.init();
		titleX = this.playerInventoryTitleX;
		this.playerInventoryTitleY = this.backgroundHeight - 94;
	}

	@Override
	protected void drawBackground (MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
		RenderSystem.setShaderTexture(0,TEXTURE);
		int x = (this.width-this.backgroundWidth)/2;
		int y = (this.height-this.backgroundHeight)/2;
		this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
		this.drawTexture(matrices, x, y + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);
	}

	@Override
	public void render (MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices,mouseX,mouseY);
	}
}
