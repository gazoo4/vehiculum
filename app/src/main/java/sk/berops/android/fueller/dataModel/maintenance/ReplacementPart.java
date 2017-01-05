package sk.berops.android.fueller.dataModel.maintenance;

import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.Map;

public class ReplacementPart extends GenericPart {

	@Element(name="originality")
	private Originality originality;
	@Element(name="quantity", required=false)
	protected int quantity;
	
	public enum Originality{
		OEM(0, "oem"),
		AFTERMARKET(1, "aftermarket"),
		UNKNOWN(2, "unknown");
		private int id;
		private String originality;	
		Originality(int id, String originality) {
			this.setId(id);
			this.setOriginality(originality);
		}
		
		private static Map<Integer, Originality> idToOriginalityMapping;

		public static Originality getOriginality(int id) {
			if (idToOriginalityMapping == null) {
				initMapping();
			}
			
			Originality result = null;
			result = idToOriginalityMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToOriginalityMapping = new HashMap<Integer, ReplacementPart.Originality>();
			for (Originality originality : values()) {
				idToOriginalityMapping.put(originality.id, originality);
			}
		}
	
		public String getOrigniality() {
			return originality;
		}
		public void setOriginality(String originality) {
			this.originality = originality;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
	
	public ReplacementPart() {
		this(1);
	}
	
	public ReplacementPart(int quantity) {
		super();
		this.quantity = quantity;
	}

	public Originality getOriginality() {
		return originality;
	}

	public void setOriginality(Originality originality) {
		this.originality = originality;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}