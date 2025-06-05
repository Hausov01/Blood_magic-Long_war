package wayoftime.bloodmagic.ritual;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

/**
 * Used to set a {@link EnumRuneType} type to a given {@link BlockPos} for usage
 * in Ritual creation.
 */
public class RitualComponent
{
	private final BlockPos offset;
	private final EnumRuneType runeType;

	public RitualComponent(BlockPos offset, EnumRuneType runeType)
	{
		this.offset = offset;
		this.runeType = runeType;
	}

	public int getX(Direction direction)
	{
		switch (direction)
		{
		case EAST:
			return -this.getOffset().getZ();
		case SOUTH:
			return -this.getOffset().getX();
		case WEST:
			return this.getOffset().getZ();
		default:
			return this.getOffset().getX();
		}
	}

	public int getY()
	{
		return this.getOffset().getY();
	}

	public int getZ(Direction direction)
	{
		switch (direction)
		{
		case EAST:
			return this.getOffset().getX();
		case SOUTH:
			return -this.getOffset().getZ();
		case WEST:
			return -this.getOffset().getX();
		default:
			return this.getOffset().getZ();
		}
	}

	public BlockPos getOffset(Direction direction)
	{
		return new BlockPos(getX(direction), offset.getY(), getZ(direction));
	}

	public BlockPos getOffset()
	{
		return offset;
	}

	public EnumRuneType getRuneType()
	{
		return runeType;
	}
}