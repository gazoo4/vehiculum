package sk.berops.android.fueller.dataModel;

public class Tyres extends Part {
	private Category category;
	public enum Category {
		WINTER("winter"), SUMMER("summer"), ALLSEASON("allseason");
		private String value;
		
		private Category(String value) {
			this.value = value;
		}
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
}