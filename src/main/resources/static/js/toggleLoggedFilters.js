/************************************** DESKTOP *************************************/

function toggleLoggedFilters() {
    const loggedFiltersDiv = document.getElementById("loggedSearch");
    loggedFiltersDiv.style.display = (loggedFiltersDiv.style.display === "none" || loggedFiltersDiv.style.display === "") ? "block" : "none";
    
    toggleButton.classList.toggle('rotate180')
}
const toggleButton = document.getElementById("toggleLoggedFiltersButton");
if(toggleButton) {
	toggleButton.addEventListener("click", toggleLoggedFilters);	
}

/************** Change filters mode **************/

function sellMode() {
	for(let i=0 ; i < buyCheckBoxes.length ; i++) {
		buyCheckBoxes[i].checked = false;
		buyCheckBoxes[i].disabled = true;
	}
	for(let i=0 ; i < sellCheckBoxes.length ; i++) {
		sellCheckBoxes[i].disabled = false;
	}
}

function buyMode() {
	for(let i=0 ; i < sellCheckBoxes.length ; i++) {
		sellCheckBoxes[i].checked = false;
		sellCheckBoxes[i].disabled = true;
	}
	for(let i=0 ; i < buyCheckBoxes.length ; i++) {
		buyCheckBoxes[i].disabled = false;
	}
}

/** INITIAL CALL **/

const buyRadioButton = document.getElementById('buyRadioButton');
const sellRadioButton = document.getElementById('sellRadioButton');

const buyCheckBoxes = document.querySelectorAll('.buyCheckBox');
const sellCheckBoxes = document.querySelectorAll('.sellCheckBox');

if(sellRadioButton) {
	sellRadioButton.addEventListener("click", sellMode);
}
if(buyRadioButton) {
	buyRadioButton.addEventListener("click", buyMode);
}


/************************************** MOBILE *************************************/

const purchasesFiltersButton = document.getElementById("purchasesFiltersButton");
const purchasesFilters = document.getElementById("purchases");

const mySalesFiltersButton = document.getElementById("mySalesFiltersButton");
const mySalesFilters = document.getElementById("mySales");


purchasesFiltersButton.addEventListener("click", function() {
	purchasesFiltersButton.disabled = true;
	purchasesFilters.style.display = "flex";
	buyRadioButton.click();
		
	mySalesFiltersButton.disabled = false;
	mySalesFilters.style.display = "none";
	
})

mySalesFiltersButton.addEventListener("click", function() {
	mySalesFiltersButton.disabled = true;
	mySalesFilters.style.display = "flex";
	sellRadioButton.click();
	
	purchasesFiltersButton.disabled = false;
	purchasesFilters.style.display = "none";
})

if(mySalesFilters){
	if(mySalesFilters.getAttribute('data-selected') === "true"){
		mySalesFiltersButton.click()
	}
}
