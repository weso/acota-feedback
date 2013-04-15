package es.weso.acota.core.entity.persistence;

import java.util.Date;

/**
 * Feedback contains data related to a feedback by the user.
 * 
 * @author César Luis Alvargonzález
 *
 */
public class Feedback {
	protected int id;
	protected int userId;
	protected String label;
	protected String document;
	protected Date date;

	/**
	 * Zero-argument default constructor.
	 */
	public Feedback() {
		super();
	}

	/**
	 * Five-argument secondary constructor.
	 * @param id Feedback's id
	 * @param userId Feedback's user's id
	 * @param label Feedback's Label Name
	 * @param document Feedback's Document URL
	 * @param date Feedback's Tagged Date
	 */
	public Feedback(int id, int userId, String label, String document, Date date) {
		super();
		this.id = id;
		this.userId = userId;
		this.label = label;
		this.document = document;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((document == null) ? 0 : document.hashCode());
		result = prime * result + id;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Feedback other = (Feedback) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		if (id != other.id)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Feedback [id=" + id + ", userId=" + userId + ", label=" + label
				+ ", document=" + document + ", date=" + date + "]";
	}

}
