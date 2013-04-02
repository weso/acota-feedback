package es.weso.acota.core.entity.persistence.tables;

/**
 * LabelTable contains the name of the Label table and its attributes
 * @author César Luis Alvargonzález
 *
 */
public class LabelTable {
	protected String name;

	protected String idAttribute;
	protected String nameAttribute;

	/**
	 * Zero-argument default constructor.
	 */
	public LabelTable() {
		super();
	}

	/**
	 * Three-argument secondary constructor.
	 * @param name Name of the label table within the scheme
	 * @param idAttribute Label's id attribute
	 * @param nameAttribute Label's attribute
	 */
	public LabelTable(String name, String idAttribute, String nameAttribute) {
		super();
		this.name = name;
		this.idAttribute = idAttribute;
		this.nameAttribute = nameAttribute;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdAttribute() {
		return idAttribute;
	}

	public void setIdAttribute(String idAttribute) {
		this.idAttribute = idAttribute;
	}

	public String getNameAttribute() {
		return nameAttribute;
	}

	public void setNameAttribute(String nameAttribute) {
		this.nameAttribute = nameAttribute;
	}

	@Override
	public String toString() {
		return "LabelTable [name=" + name + ", idAttribute=" + idAttribute
				+ ", nameAttribute=" + nameAttribute + "]";
	}

}
