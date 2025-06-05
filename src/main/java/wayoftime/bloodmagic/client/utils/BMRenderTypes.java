package wayoftime.bloodmagic.client.utils;

public class BMRenderTypes
{
//	public static final RenderType SOLID_FULLBRIGHT;
//	public static final RenderType TRANSLUCENT_LINES;
//	public static final RenderType LINES;
//	public static final RenderType TRANSLUCENT_TRIANGLES;
//	public static final RenderType TRANSLUCENT_POSITION_COLOR;
//	public static final RenderType TRANSLUCENT_NO_DEPTH;
//	public static final RenderType CHUNK_MARKER;
//	public static final RenderType VEIN_MARKER;
//	public static final RenderType POSITION_COLOR_TEX_LIGHTMAP;
//	public static final RenderType POSITION_COLOR_LIGHTMAP;
//	public static final RenderType ITEM_DAMAGE_BAR;
//	protected static final RenderStateShard.ShadeModelStateShard SHADE_ENABLED = new RenderStateShard.ShadeModelStateShard(true);
//	protected static final RenderStateShard.TextureStateShard BLOCK_SHEET_MIPPED = new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, true);
//	protected static final RenderStateShard.LightmapStateShard LIGHTMAP_DISABLED = new RenderStateShard.LightmapStateShard(false);
//	protected static final RenderStateShard.TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
//		RenderSystem.enableBlend();
//		RenderSystem.defaultBlendFunc();
//	}, RenderSystem::disableBlend);
//	protected static final RenderStateShard.TransparencyStateShard NO_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("no_transparency", RenderSystem::disableBlend, () -> {
//	});
//	protected static final RenderStateShard.DepthTestStateShard DEPTH_ALWAYS = new RenderStateShard.DepthTestStateShard("", GL11.GL_ALWAYS);
//
//	static
//	{
//		RenderType.CompositeState fullbrightSolidState = RenderType.CompositeState.builder().setShadeModelState(SHADE_ENABLED).setLightmapState(LIGHTMAP_DISABLED).setTextureState(BLOCK_SHEET_MIPPED).createCompositeState(true);
//		SOLID_FULLBRIGHT = RenderType.create(BloodMagic.MODID + ":block_fullbright", DefaultVertexFormat.BLOCK, GL11.GL_QUADS, 256, fullbrightSolidState);
//		RenderType.CompositeState translucentNoDepthState = RenderType.CompositeState.builder().setTransparencyState(TRANSLUCENT_TRANSPARENCY).setLineState(new LineStateShard(OptionalDouble.of(2))).setTextureState(new TextureStateShard()).setDepthTestState(DEPTH_ALWAYS).createCompositeState(false);
//		RenderType.CompositeState translucentNoTextureState = RenderType.CompositeState.builder().setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTextureState(new TextureStateShard()).createCompositeState(false);
//		TRANSLUCENT_LINES = RenderType.create(BloodMagic.MODID + ":translucent_lines", DefaultVertexFormat.POSITION_COLOR, GL11.GL_LINES, 256, translucentNoDepthState);
//		LINES = RenderType.create(BloodMagic.MODID + ":lines", DefaultVertexFormat.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.CompositeState.builder().createCompositeState(false));
//		TRANSLUCENT_TRIANGLES = RenderType.create(BloodMagic.MODID + ":translucent_triangle_fan", DefaultVertexFormat.POSITION_COLOR, GL11.GL_TRIANGLES, 256, translucentNoDepthState);
//		TRANSLUCENT_POSITION_COLOR = RenderType.create(BloodMagic.MODID + ":translucent_pos_color", DefaultVertexFormat.POSITION_COLOR, GL11.GL_QUADS, 256, translucentNoTextureState);
//		TRANSLUCENT_NO_DEPTH = RenderType.create(BloodMagic.MODID + ":translucent_no_depth", DefaultVertexFormat.POSITION_COLOR, GL11.GL_QUADS, 256, translucentNoDepthState);
//		RenderType.CompositeState chunkMarkerState = RenderType.CompositeState.builder().setTextureState(new TextureStateShard()).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(new CullStateShard(false)).setShadeModelState(new ShadeModelStateShard(true)).setLineState(new LineStateShard(OptionalDouble.of(5))).createCompositeState(false);
//		CHUNK_MARKER = RenderType.create(BloodMagic.MODID + ":chunk_marker", DefaultVertexFormat.POSITION_COLOR, GL11.GL_LINES, 256, chunkMarkerState);
//		VEIN_MARKER = RenderType.create(BloodMagic.MODID + ":vein_marker", DefaultVertexFormat.POSITION_COLOR, GL11.GL_LINE_LOOP, 256, chunkMarkerState);
//		POSITION_COLOR_TEX_LIGHTMAP = RenderType.create(BloodMagic.MODID + ":pos_color_tex_lightmap", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 256, RenderType.CompositeState.builder().setTextureState(new TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false)).setLightmapState(new LightmapStateShard(true)).createCompositeState(false));
//		POSITION_COLOR_LIGHTMAP = RenderType.create(BloodMagic.MODID + ":pos_color_lightmap", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, GL11.GL_QUADS, 256, RenderType.CompositeState.builder().setTextureState(new TextureStateShard()).setLightmapState(new LightmapStateShard(true)).createCompositeState(false));
//		ITEM_DAMAGE_BAR = RenderType.create(BloodMagic.MODID + ":item_damage_bar", DefaultVertexFormat.POSITION_COLOR, GL11.GL_QUADS, 256, RenderType.CompositeState.builder().setDepthTestState(DEPTH_ALWAYS).setTextureState(new TextureStateShard()).setAlphaState(new AlphaStateShard(0)).setTransparencyState(NO_TRANSPARENCY).createCompositeState(false));
//	}
////
//	public static RenderType getGui(ResourceLocation texture)
//	{
////		setTransparencyState(RenderStateShard.TransparencyStateShard
//		return RenderType.create("gui_" + texture, DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setTextureState(new TextureStateShard(texture, false, false)).createCompositeState(false));
//	}
//
//	public static RenderType getLines(float lineWidth)
//	{
//		return RenderType.create("lines_color_pos_" + lineWidth, DefaultVertexFormat.POSITION_COLOR, GL11.GL_LINES, 256, RenderType.CompositeState.builder().setLineState(new LineStateShard(OptionalDouble.of(lineWidth))).setTextureState(new TextureStateShard()).createCompositeState(false));
//	}
//
//	public static RenderType getPoints(float pointSize)
//	{
//		// Not really a fog state, but using it like this makes using RenderType.State
//		// with custom states possible
//		FogStateShard setPointSize = new FogStateShard(BloodMagic.MODID + ":pointsize_" + pointSize, () -> GL11.glPointSize(pointSize), () -> {
//			GL11.glPointSize(1);
//		});
//		return RenderType.create("point_pos_color_" + pointSize, DefaultVertexFormat.POSITION_COLOR, GL11.GL_POINTS, 256, RenderType.CompositeState.builder().setFogState(setPointSize).setTextureState(new TextureStateShard()).createCompositeState(false));
//	}
//
//	public static RenderType getPositionTex(ResourceLocation texture)
//	{
//		return RenderType.create(BloodMagic.MODID + ":pos_tex_" + texture, DefaultVertexFormat.POSITION_TEX, GL11.GL_QUADS, 256, RenderType.CompositeState.builder().setTextureState(new TextureStateShard(texture, false, false)).createCompositeState(false));
//	}
//
//	public static RenderType getFullbrightTranslucent(ResourceLocation resourceLocation)
//	{
//		RenderType.CompositeState glState = RenderType.CompositeState.builder().setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTextureState(new TextureStateShard(resourceLocation, false, false)).setLightmapState(new LightmapStateShard(false)).createCompositeState(false);
//		return RenderType.create("BloodMagic:fullbright_translucent_" + resourceLocation, DefaultVertexFormat.BLOCK, GL11.GL_QUADS, 256, glState);
//	}
//
//	public static MultiBufferSource wrapWithStencil(MultiBufferSource in, Consumer<VertexConsumer> setupStencilArea, String name, int ref)
//	{
//		return wrapWithAdditional(in, "stencil_" + name + "_" + ref, () -> {
//			GL11.glEnable(GL11.GL_STENCIL_TEST);
//			RenderSystem.colorMask(false, false, false, false);
//			RenderSystem.depthMask(false);
//			GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
//			GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
//
//			GL11.glStencilMask(0xFF);
//			RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, true);
//			RenderSystem.disableTexture();
//			Tesselator tes = Tesselator.getInstance();
//			BufferBuilder bb = tes.getBuilder();
//			bb.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);
//			setupStencilArea.accept(bb);
//			tes.end();
//			RenderSystem.enableTexture();
//			RenderSystem.colorMask(true, true, true, true);
//			RenderSystem.depthMask(true);
//			GL11.glStencilMask(0x00);
//			GL11.glStencilFunc(GL11.GL_EQUAL, ref, 0xFF);
//		}, () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
//	}
//
//	public static MultiBufferSource disableLighting(MultiBufferSource in)
//	{
//		return wrapWithAdditional(in, "no_lighting", RenderSystem::disableLighting, () -> {
//		});
//	}
//
//	public static MultiBufferSource disableCull(MultiBufferSource in)
//	{
//		return wrapWithAdditional(in, "no_cull", RenderSystem::disableCull, RenderSystem::enableCull);
//	}
//
//	private static MultiBufferSource wrapWithAdditional(MultiBufferSource in, String name, Runnable setup, Runnable teardown)
//	{
//		return type -> {
//			return in.getBuffer(new RenderType(BloodMagic.MODID + ":" + type + "_" + name, type.format(), type.mode(), type.bufferSize(), type.affectsCrumbling(), false, () -> {
//				type.setupRenderState();
//				setup.run();
//			}, () -> {
//				teardown.run();
//				type.clearRenderState();
//			})
//			{
//			});
//		};
//	}
}
