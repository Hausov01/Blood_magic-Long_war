package wayoftime.bloodmagic.common.item.soul;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWill;
import wayoftime.bloodmagic.api.compat.IDemonWillWeapon;
import wayoftime.bloodmagic.api.compat.IMultiWillTool;
import wayoftime.bloodmagic.common.item.BMItemTier;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ItemSentientSword extends SwordItem implements IDemonWillWeapon, IMultiWillTool
{
	public static int[] soulBracket = new int[] { 16*4, 60*4, 200*4, 400*4, 1000*4, 2000*4, 4000*2 };
	public static double[] defaultDamageAdded = new double[] { 1*0.5, 1.5*0.75, 2*0.8, 2.5, 3, 3.5, 4 };
	public static double[] destructiveDamageAdded = new double[] { 1.5*0.5, 2.5*0.75, 3*0.8, 3.75, 4.5, 5.25, 6 };
	public static double[] vengefulDamageAdded = new double[] { 0, 0.5*0.75, 1*0.8, 1.5, 2, 2.25, 2.5 };
	public static double[] steadfastDamageAdded = new double[] { 0, 0.5*0.75, 1*0.8, 1.5, 2, 2.25, 2.5 };
	public static double[] soulDrainPerSwing = new double[] { 0.05, 0.1, 0.2, 0.4, 0.75, 1, 1.25 };
	public static double[] soulDrop = new double[] { 2, 4, 7, 10, 13, 15, 18 };
	public static double[] staticDrop = new double[] { 1, 1, 2, 3, 3, 4, 4 };

	public static double[] healthBonus = new double[] { 0, 0, 0, 0, 0, 0, 0 }; // TODO: Think of implementing this later
	public static double[] vengefulAttackSpeed = new double[] { -2.1, -2, -1.8, -1.7, -1.6, -1.6, -1.5 };
	public static double[] destructiveAttackSpeed = new double[] { -2.6, -2.7, -2.8, -2.9, -3, -3, -3 };

	public static int[] absorptionTime = new int[] { 200, 300, 400, 500, 600, 700, 800 };

	public static double maxAbsorptionHearts = 10;

	public static int[] poisonTime = new int[] { 25, 50, 60, 80, 100, 120, 150 };
	public static int[] poisonLevel = new int[] { 0, 0, 0, 1, 1, 1, 1 };

	public static double[] movementSpeed = new double[] { 0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.4 };

	public ItemSentientSword()
	{
//		super(RegistrarBloodMagicItems.SOUL_TOOL_MATERIAL);
		super(BMItemTier.SENTIENT, 6, -2.6f, new Item.Properties().durability(520));
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair)
	{
		return repair.is(BloodMagicTags.CRYSTAL_DEMON) || super.isValidRepairItem(toRepair, repair);
	}

	public void recalculatePowers(ItemStack stack, Level world, Player player)
	{
		EnumDemonWillType type = PlayerDemonWillHandler.getLargestWillType(player);
		double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);
		recalculatePowers(stack, type, soulsRemaining);
	}

	public void recalculatePowers(ItemStack stack, EnumDemonWillType type, double will)
	{
		this.setCurrentType(stack, will > 0 ? type : EnumDemonWillType.DEFAULT);
		int level = getLevel(stack, will);

		double drain = level >= 0 ? soulDrainPerSwing[level] : 0;
		double extraDamage = getExtraDamage(type, level);

		setActivatedState(stack, will > 16);

		setDrainOfActivatedSword(stack, drain);
		setDamageOfActivatedSword(stack, 5 + extraDamage);
		setStaticDropOfActivatedSword(stack, level >= 0 ? staticDrop[level] : 1);
		setDropOfActivatedSword(stack, level >= 0 ? soulDrop[level] : 0);
		setAttackSpeedOfSword(stack, level >= 0 ? getAttackSpeed(type, level) : -2.4);
		setHealthBonusOfSword(stack, level >= 0 ? getHealthBonus(type, level) : 0);
		setSpeedOfSword(stack, level >= 0 ? getMovementSpeed(type, level) : 0);
	}

	public boolean getActivated(ItemStack stack)
	{
		return !stack.isEmpty() && NBTHelper.checkNBT(stack).getTag().getBoolean(Constants.NBT.ACTIVATED);
	}

	public ItemStack setActivatedState(ItemStack stack, boolean activated)
	{
		if (!stack.isEmpty())
		{
			NBTHelper.checkNBT(stack).getTag().putBoolean(Constants.NBT.ACTIVATED, activated);
			return stack;
		}

		return stack;
	}

	public double getExtraDamage(EnumDemonWillType type, int willBracket)
	{
		if (willBracket < 0)
		{
			return 0;
		}

		switch (type)
		{
		case CORROSIVE:
		case DEFAULT:
			return defaultDamageAdded[willBracket] * 4.5;
		case DESTRUCTIVE:
			return destructiveDamageAdded[willBracket] * 4.5;
		case VENGEFUL:
			return vengefulDamageAdded[willBracket] * 4.5;
		case STEADFAST:
			return steadfastDamageAdded[willBracket] * 4.5;
		}

		return 0;
	}

	public double getAttackSpeed(EnumDemonWillType type, int willBracket)
	{
		switch (type)
		{
		case VENGEFUL:
			return vengefulAttackSpeed[willBracket];
		case DESTRUCTIVE:
			return destructiveAttackSpeed[willBracket];
		default:
			return -2.4;
		}
	}

	public double getHealthBonus(EnumDemonWillType type, int willBracket)
	{
		switch (type)
		{
		case STEADFAST:
			return healthBonus[willBracket];
		default:
			return 0;
		}
	}

	public double getMovementSpeed(EnumDemonWillType type, int willBracket)
	{
		switch (type)
		{
		case VENGEFUL:
			return movementSpeed[willBracket];
		default:
			return 0;
		}
	}

	public void applyEffectToEntity(EnumDemonWillType type, int willBracket, LivingEntity target, LivingEntity attacker)
	{
		switch (type)
		{
		case CORROSIVE:
			target.addEffect(new MobEffectInstance(MobEffects.WITHER, poisonTime[willBracket], poisonLevel[willBracket]));
			break;
		case DEFAULT:
			break;
		case DESTRUCTIVE:
			break;
		case STEADFAST:
			if (!target.isAlive())
			{
				float absorption = attacker.getAbsorptionAmount();
				attacker.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, absorptionTime[willBracket], 127, false, false));
				attacker.setAbsorptionAmount((float) Math.min(absorption + target.getMaxHealth() * 0.05f, maxAbsorptionHearts));
			}
			break;
		case VENGEFUL:
			break;
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		if (super.hurtEnemy(stack, target, attacker))
		{
			if (attacker instanceof Player)
			{
				Player attackerPlayer = (Player) attacker;
				this.recalculatePowers(stack, attackerPlayer.getCommandSenderWorld(), attackerPlayer);
				EnumDemonWillType type = this.getCurrentType(stack);
				double will = PlayerDemonWillHandler.getTotalDemonWill(type, attackerPlayer);
				int willBracket = this.getLevel(stack, will);

				applyEffectToEntity(type, willBracket, target, attackerPlayer);

//				ItemStack offStack = attackerPlayer.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
//				if (offStack.getItem() instanceof ISentientSwordEffectProvider)
//				{
//					ISentientSwordEffectProvider provider = (ISentientSwordEffectProvider) offStack.getItem();
//					if (provider.providesEffectForWill(type))
//					{
//						provider.applyOnHitEffect(type, stack, offStack, attacker, target);
//					}
//				}
			}

			return true;
		}

		return false;
	}

	@Override
	public EnumDemonWillType getCurrentType(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		if (!tag.contains(Constants.NBT.WILL_TYPE))
		{
			return EnumDemonWillType.DEFAULT;
		}

		return EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ROOT));
	}

	public void setCurrentType(ItemStack stack, EnumDemonWillType type)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putString(Constants.NBT.WILL_TYPE, type.toString());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		recalculatePowers(player.getItemInHand(hand), world, player);
		return super.use(world, player, hand);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return oldStack.getItem() != newStack.getItem();
	}

	private int getLevel(ItemStack stack, double soulsRemaining)
	{
		int lvl = -1;
		for (int i = 0; i < soulBracket.length; i++)
		{
			if (soulsRemaining >= soulBracket[i])
			{
				lvl = i;
			}
		}

		return lvl;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		if (!stack.hasTag())
			return;

//		tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.sentientSword.desc"))));
		tooltip.add(Component.translatable("tooltip.bloodmagic.sentientSword.desc").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("tooltip.bloodmagic.currentType." + getCurrentType(stack).name().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity)
	{
		recalculatePowers(stack, player.getCommandSenderWorld(), player);

		double drain = this.getDrainOfActivatedSword(stack);
		if (drain > 0)
		{
			EnumDemonWillType type = getCurrentType(stack);
			double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);

			if (drain > soulsRemaining)
			{
				return false;
			} else
			{
				PlayerDemonWillHandler.consumeDemonWill(type, player, drain);
			}
		}

		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public List<ItemStack> getRandomDemonWillDrop(LivingEntity killedEntity, LivingEntity attackingEntity, ItemStack stack, int looting)
	{
		List<ItemStack> soulList = new ArrayList<>();

		if (killedEntity.getCommandSenderWorld().getDifficulty() != Difficulty.PEACEFUL && !(killedEntity instanceof Enemy))
		{
			return soulList;
		}

		double willModifier = killedEntity instanceof Slime ? 0.67 : 1;

		IDemonWill soul;

		EnumDemonWillType type = this.getCurrentType(stack);
		switch (type)
		{
		case CORROSIVE:
			soul = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_CORROSIVE.get());
			break;
		case DESTRUCTIVE:
			soul = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_DESTRUCTIVE.get());
			break;
		case STEADFAST:
			soul = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_STEADFAST.get());
			break;
		case VENGEFUL:
			soul = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_VENGEFUL.get());
			break;
		default:
			soul = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_RAW.get());
			break;
		}

		for (int i = 0; i <= looting; i++)
		{
			if (i == 0 || attackingEntity.getCommandSenderWorld().random.nextDouble() < 0.4)
			{
				ItemStack soulStack = soul.createWill(willModifier * (this.getDropOfActivatedSword(stack) * attackingEntity.getCommandSenderWorld().random.nextDouble() + this.getStaticDropOfActivatedSword(stack)) * killedEntity.getMaxHealth() / 20d);
				soulList.add(soulStack);
			}
		}

		return soulList;
	}

	// TODO: Change attack speed.
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
	{
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
		if (slot == EquipmentSlot.MAINHAND)
		{
			multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", getDamageOfActivatedSword(stack), AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.getAttackSpeedOfSword(stack), AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.MAX_HEALTH, new AttributeModifier(new UUID(0, 31818145), "Weapon modifier", this.getHealthBonusOfSword(stack), AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(new UUID(0, 4218052), "Weapon modifier", this.getSpeedOfSword(stack), AttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}

	public double getDamageOfActivatedSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_DAMAGE);
	}

	public void setDamageOfActivatedSword(ItemStack stack, double damage)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_DAMAGE, damage);
	}

	public double getDrainOfActivatedSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_ACTIVE_DRAIN);
	}

	public void setDrainOfActivatedSword(ItemStack stack, double drain)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_ACTIVE_DRAIN, drain);
	}

	public double getStaticDropOfActivatedSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_STATIC_DROP);
	}

	public void setStaticDropOfActivatedSword(ItemStack stack, double drop)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_STATIC_DROP, drop);
	}

	public double getDropOfActivatedSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_DROP);
	}

	public void setDropOfActivatedSword(ItemStack stack, double drop)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_DROP, drop);
	}

	public double getHealthBonusOfSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_HEALTH);
	}

	public void setHealthBonusOfSword(ItemStack stack, double hp)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_HEALTH, hp);
	}

	public double getAttackSpeedOfSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_ATTACK_SPEED);
	}

	public void setAttackSpeedOfSword(ItemStack stack, double speed)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_ATTACK_SPEED, speed);
	}

	public double getSpeedOfSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_SPEED);
	}

	public void setSpeedOfSword(ItemStack stack, double speed)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_SPEED, speed);
	}

}
