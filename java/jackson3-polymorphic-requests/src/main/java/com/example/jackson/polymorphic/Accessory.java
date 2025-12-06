package com.example.jackson.polymorphic;

import java.util.List;

public record Accessory(
		String brand,
		String manufacturer,
		String model,
		double price,
		List<String> specialFeatures
) implements CartItem {

	@Override
	public ItemCategory itemCategory() {
		return ItemCategory.ACCESSORY;
	}
}
