package com.africaapps.league.model.league;

public enum BlockType {

	GOALKEEPER,
	DEFENDER,
	MIDFIELDER,
	STRIKER,
	SUBSTITUTE;
	
	public static BlockType convert(String t) {
		if (t != null) {
			for(BlockType bt : values()) {
				if (bt.name().equalsIgnoreCase(t)) {
					return bt;
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
}
