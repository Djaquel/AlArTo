const membersListButton = document.getElementById('membersListButton');
const membersList = document.getElementById('membersList');

const categoriesListButton = document.getElementById('categoriesListButton');
const categoriesList = document.getElementById('categoriesList');

membersListButton.addEventListener("click", function() {
	membersListButton.disabled = true;
	membersList.style.display = "block";
	
	categoriesListButton.disabled = false;
	categoriesList.style.display = "none";
})

categoriesListButton.addEventListener("click", function() {
	membersListButton.disabled = false;
	membersList.style.display = "none";
	
	categoriesListButton.disabled = true;
	categoriesList.style.display = "block";
})