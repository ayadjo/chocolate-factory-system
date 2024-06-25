package dto;

public class RejectPurchaseDTO {
	private Long purchaseId;
	private String rejectionNote;
	
	public RejectPurchaseDTO() {
		super();
	}

	public RejectPurchaseDTO(Long purchaseId, String rejectionNote) {
		super();
		this.purchaseId = purchaseId;
		this.rejectionNote = rejectionNote;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getRejectionNote() {
		return rejectionNote;
	}

	public void setRejectionNote(String rejectionNote) {
		this.rejectionNote = rejectionNote;
	}
	
	
}
