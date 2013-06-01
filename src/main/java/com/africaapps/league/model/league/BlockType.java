package com.africaapps.league.model.league;

public enum BlockType {

	Goalkeeper(9),
	Defender(10),
	Midfielder(11),
	Striker(12);
	
	private int block;
	
	BlockType(int block) {
		this.block = block;
	}
	
	public static BlockType getBlock(int b) {
		for (BlockType blockType : values()) {
			if (blockType.getBlock() == b) {
				return blockType;
			}
		}
		return null;
	}
	
	public int getBlock() {
		return block;
	}
}
