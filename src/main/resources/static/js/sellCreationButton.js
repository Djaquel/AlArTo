// SELL CREATION PAGE
const sellCreationButton = document.getElementById('sellCreation');
const sellCreationButtonMock = document.getElementById('sellCreationMock');

sellCreationButtonMock.addEventListener("click", function() {
	if(confirm('Sure everything\'s OK ? Once a sell started, you won\'t be able to edit it.')) {
		sellCreationButton.click();
	}
})