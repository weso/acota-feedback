package es.weso.acota.core.entity.persistence.tables;

/**
 * DocumentTable contains the name of the Document table and its attributes
 * @author César Luis Alvargonzález
 *
 */
public class DocumentTable {
	protected String name;
	protected String idAttribute;
	protected String nameAttribute;
	protected String timestampAttribute;
	
	/**
	 * Zero-argument default constructor.
	 */
	public DocumentTable() {
		super();
	}

	/**
	 * Four-argument secondary constructor.
	 * @param name Name of the document table within the scheme
	 * @param idAttribute Document's id attribute
	 * @param nameAttribute Document's name attribute
	 * @param timestampAttribute Document's Timestamp attribute
	 */
	public DocumentTable(String name, String idAttribute, String nameAttribute,String timestampAttribute) {
		super();
		this.name = name;
		this.idAttribute = idAttribute;
		this.nameAttribute = nameAttribute;
		this.timestampAttribute = timestampAttribute;
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

	public String getTimestampAttribute() {
		return timestampAttribute;
	}

	public void setTimestampAttribute(String timestampAttribute) {
		this.timestampAttribute = timestampAttribute;
	}

	@Override
	public String toString() {
		return "DocumentTable [name=" + name + ", idAttribute=" + idAttribute
				+ ", nameAttribute=" + nameAttribute + ", timestampAttribute="
				+ timestampAttribute + "]";
	}

}
