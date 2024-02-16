const hiddenButtons = document.querySelectorAll('#hiddenButton');
const hiddenButtonMocks = document.querySelectorAll('#hiddenButtonMock');

hiddenButtonMocks.forEach(function(hiddenButtonMock, index) {
    hiddenButtonMock.addEventListener("click", function() {
        // Make sure the corresponding hiddenButton exists at the same index
        if (hiddenButtons[index]) {
            hiddenButtons[index].click();
        }
    });
});
