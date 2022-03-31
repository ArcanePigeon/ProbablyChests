package org.cloudwarp.probablychests.client;


import org.cloudwarp.probablychests.block.entity.PCChestBlockEntity;
import org.cloudwarp.probablychests.client.entity.model.PCChestBlockModel;
import software.bernie.example.client.model.tile.BotariumModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class PCBlockEntityRenderer extends GeoBlockRenderer<PCChestBlockEntity> {

	public PCBlockEntityRenderer () {
		super(new PCChestBlockModel());
	}
}
