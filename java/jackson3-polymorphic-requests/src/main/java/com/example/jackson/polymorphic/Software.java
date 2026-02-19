package com.example.jackson.polymorphic;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("SOFTWARE")
public record Software(
		String os,
		String manufacturer,
		String title,
		double price,
		String version
) implements CartItem {

	@Override
	public ItemCategory itemCategory() {
		return ItemCategory.SOFTWARE;
	}
}
