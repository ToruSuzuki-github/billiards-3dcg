import javax.media.j3d.*;

public class BilliardsAppearance {
	private Appearance appearance;
	private Material material;

	public BilliardsAppearance() {
		appearance = new Appearance();
		material = new Material();
		appearance.setMaterial(material);
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public Material getMaterial() {
		return material;
	}
}
