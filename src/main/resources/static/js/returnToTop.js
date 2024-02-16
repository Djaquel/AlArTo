window.addEventListener('scroll', function() {
	const button = document.querySelector('.return-to-top');
	if(window.pageYOffset > 100) {
		button.style.display = 'block';
	} else {
		button.style.display = 'none';
	}
});

document.querySelector('.return-to-top').addEventListener('click', function(e) {
	e.preventDefault();
	window.scrollTo({top: 0, behavior: 'smooth'});
});