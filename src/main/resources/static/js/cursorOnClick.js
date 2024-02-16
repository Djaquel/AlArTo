const customCursorElement = document.querySelectorAll('.custom-cursor-element');

customCursorElement.forEach(function(element) {
	element.addEventListener("mousedown", function() {
		element.style.cursor = 'url("/images/cursor/cursor_clicked.cur") 16 16, auto';
	})
	
		element.addEventListener("mouseup", function() {
		element.style.cursor = 'url("/images/cursor/cursor.cur") 16 16, auto';
	})
})