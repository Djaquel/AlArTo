function toggleProfileSells() {
	const profileSellsDiv = document.getElementById('profileSells');
	profileSellsDiv.style.display = profileSellsDiv.style.display === "none" ||
	profileSellsDiv.style.display === "" ? "block" : "none";
	
	toggleProfileSellsButton.classList.toggle('rotate180');
}
 
 
 const toggleProfileSellsButton = document.getElementById('toggleProfileSellsButton');
 toggleProfileSellsButton.addEventListener("click", toggleProfileSells);
 
 
 
 
 
 
 
 
 
 
