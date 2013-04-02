package es.weso.acota.core.entity.persistence.tables;

/**
 * FeedbackTable contains the name of the Feedback table and its attributes
 * @author César Luis Alvargonzález
 *
 */
public class FeedbackTable {

	protected String name;
	protected String idAttribute;
	protected String userIdAttribute;
	protected String documentIdAttribute;
	protected String labelIdAttribute;
	protected String preferenceAttribute;
	protected String timestampAttribute;

	/**
	 * Zero-argument default constructor.
	 */
	public FeedbackTable() {
		super();
	}

	/**
	 * Seven-argument constructor.
	 * @param name Name of the feedback table within the scheme
	 * @param idAttribute Id attribute
	 * @param userIdAttribute User id attribute
	 * @param documentIdAttribute Document attribute
	 * @param labelIdAttribute Label attribute
	 * @param preferenceAttribute Preference attribute
	 * @param timestampAttribute Timestamp attribute
	 */
	public FeedbackTable(String name, String idAttribute, String userIdAttribute,
			String documentIdAttribute, String labelIdAttribute,
			String preferenceAttribute, String timestampAttribute) {
		super();
		this.name = name;
		this.idAttribute = idAttribute;
		this.userIdAttribute = userIdAttribute;
		this.documentIdAttribute = documentIdAttribute;
		this.labelIdAttribute = labelIdAttribute;
		this.preferenceAttribute = preferenceAttribute;
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

	public String getUserIdAttribute() {
		return userIdAttribute;
	}

	public void setUserIdAttribute(String userIdAttribute) {
		this.userIdAttribute = userIdAttribute;
	}

	public String getDocumentIdAttribute() {
		return documentIdAttribute;
	}

	public void setDocumentIdAttribute(String documentIdAttribute) {
		this.documentIdAttribute = documentIdAttribute;
	}

	public String getLabelIdAttribute() {
		return labelIdAttribute;
	}

	public void setLabelIdAttribute(String labelIdAttribute) {
		this.labelIdAttribute = labelIdAttribute;
	}

	public String getPreferenceAttribute() {
		return preferenceAttribute;
	}

	public void setPreferenceAttribute(String preferenceAttribute) {
		this.preferenceAttribute = preferenceAttribute;
	}

	public String getTimestampAttribute() {
		return timestampAttribute;
	}

	public void setTimestampAttribute(String timestampAttribute) {
		this.timestampAttribute = timestampAttribute;
	}

	@Override
	public String toString() {
		return "FeedbackTable [name=" + name + ", idField=" + idAttribute
				+ ", userIdAttribute=" + userIdAttribute
				+ ", documentIdAttribute=" + documentIdAttribute
				+ ", labelIdField=" + labelIdAttribute + ", preferenceAttribute="
				+ preferenceAttribute + ", timestampAttribute="
				+ timestampAttribute + "]";
	}

}
