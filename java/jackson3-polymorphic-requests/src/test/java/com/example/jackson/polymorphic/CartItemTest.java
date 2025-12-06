package com.example.jackson.polymorphic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.exc.InvalidTypeIdException;
import tools.jackson.databind.json.JsonMapper;

class CartItemTest {

	private static final JsonMapper mapper = new JsonMapper();

	@Test
	@DisplayName("Should parse item as Software")
	void shouldParseItemAsSoftware() {
		final var item = /* language=JSON */ """
				{
					"itemCategory": "SOFTWARE",
					"os": "Windows",
					"manufacturer": "Microsoft Software",
					"title": "Office Professional",
					"price": 6300.0,
					"version": "2021"
				}
				""";
		final var cartItem = mapper.readValue(item, CartItem.class);
		assertThat(cartItem)
				.isExactlyInstanceOf(Software.class)
				.satisfies(software -> assertThat(software.itemCategory()).isEqualTo(ItemCategory.SOFTWARE));
	}

	@Test
	@DisplayName("Should parse item as Accessory")
	void shouldParseItemAsAccessory() {
		final var item = /* language=JSON */ """
				{
					"itemCategory": "ACCESSORY",
					"brand": "Dell",
					"manufacturer": "Dell Incorporation",
					"model": "MS116-XY",
					"price": 449.0,
					"specialFeatures": ["Wired", "Optical"]
				}
				""";
		final var cartItem = mapper.readValue(item, CartItem.class);
		assertThat(cartItem)
				.isExactlyInstanceOf(Accessory.class)
				.satisfies(accessory -> assertThat(accessory.itemCategory()).isEqualTo(ItemCategory.ACCESSORY));
	}

	@Test
	@DisplayName("Should throw exception on unknown item")
	void shouldThrowExceptionOnUnknownItem() {
		final var item = /* language=JSON */ """
				{
					"itemCategory": "SNACK",
					"speciality": "Vegetarian",
					"form": "Toffee"
				}
				""";
		final Exception exception = catchException(() -> mapper.readValue(item, CartItem.class));
		assertThat(exception)
				.isExactlyInstanceOf(InvalidTypeIdException.class)
				.hasMessageStartingWith("Could not resolve type id 'SNACK'");
	}
}
