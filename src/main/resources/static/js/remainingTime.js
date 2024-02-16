const articles = document.querySelectorAll(".card");

function calculateRemainingTime(startDate, endDate) {
	
	const currentDate = new Date();
	
	// Calculate the time difference in milliseconds
	const timeDifference = endDate.getTime() - currentDate.getTime();
	
	// Convert milliseconds to minutes, hours, and days
	const totalMinutes = Math.floor(timeDifference / (1000 * 60));
	const remainingDays = Math.floor(totalMinutes / (24 * 60) + 1);
	let remainingHours, remainingMinutes;
	
	// Check if remainingDays is less than 0
	if(remainingDays <= 0 && remainingDays > -1) {
		// Calculate time until the next midnight
		let midnight = new Date();
		midnight.setHours(24, 0, 0, 0);
		const timeUntilMidnight = midnight.getTime() - currentDate.getTime();
		
		// Convert time until midnight to minutes, hours, and remaining minutes
		const totalMinutesUntilMidnight = Math.floor(timeUntilMidnight / (1000 * 60));
		remainingHours = Math.floor(totalMinutesUntilMidnight / 60);
		remainingMinutes = totalMinutesUntilMidnight % 60;
	} else {
		remainingHours = 0;
		remainingMinutes = 0;
	}
	
    return { remainingDays, remainingHours, remainingMinutes };
}

function updateHTML(remainingDays, remainingHours, remainingMinutes, article) {
	
	const remainingDaysDiv = article.querySelector(".remainingDays");
    const remainingHoursDiv = article.querySelector(".remainingHours");
    const remainingMinutesDiv = article.querySelector(".remainingMinutes");
    
    const remainingTimeDiv = article.querySelector(".remainingTime");
    const oldAuctionDiv = article.querySelector(".oldAuction");
    
    const daysDiv = article.querySelector(".days");
    const hoursDiv = article.querySelector(".hours");
    const minutesDiv = article.querySelector(".minutes");
	
	if(remainingDays === 0) {
		remainingDaysDiv.style.display = "none";
		daysDiv.style.display = "none";
	} else if(remainingDays <= -1) {
		remainingTimeDiv.style.display = "none";
		oldAuctionDiv.style.display = "block";	
	} else {
	    remainingDaysDiv.textContent = remainingDays;
	    remainingHoursDiv.style.display = "none";
	    hoursDiv.style.display = "none";
		remainingMinutesDiv.style.display = "none";
		minutesDiv.style.display = "none";	

	}

    remainingHoursDiv.textContent = remainingHours;
    remainingMinutesDiv.textContent = remainingMinutes;
}

for (let i = 0; i < articles.length; i++) {
    const article = articles[i];
    const startDate = new Date(article.querySelector(".startDate").textContent);
    const endDate = new Date(article.querySelector(".endDate").textContent);

    const { remainingDays, remainingHours, remainingMinutes } = calculateRemainingTime(startDate, endDate);

    updateHTML(remainingDays, remainingHours, remainingMinutes, article);
}