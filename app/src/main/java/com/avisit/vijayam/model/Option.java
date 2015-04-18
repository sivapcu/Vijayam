package com.avisit.vijayam.model;

public class Option {
	private int optionId;
	private int questionId;
	private String optionText;
	private boolean selectedFlag;
	private boolean correctFlag;
	
	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public boolean isSelectedFlag() {
		return selectedFlag;
	}

	public void setSelectedFlag(boolean selectedFlag) {
		this.selectedFlag = selectedFlag;
	}

	public boolean isCorrectFlag() {
		return correctFlag;
	}

	public void setCorrectFlag(boolean correctFlag) {
		this.correctFlag = correctFlag;
	}
}
