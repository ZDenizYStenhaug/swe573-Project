function showOffers() {
    const offerDiv = document.getElementById("offers");
    const eventDiv = document.getElementById("events");
    const scheduleDiv = document.getElementById("schedule");
    const offerButton = document.getElementById("offerButt");
    const eventButton = document.getElementById("eventButt");
    const scheduleButton = document.getElementById("scheduleButt");
    if (offerDiv.style.display === 'none') {
        offerDiv.style.display = 'block';
        eventDiv.style.display = "none";
        scheduleDiv.style.display = "none";
        offerButton.style.backgroundColor = "darkgrey";
        eventButton.style.backgroundColor = "transparent";
        scheduleButton.style.backgroundColor = "transparent";
    }
}
function showEvents() {
    const offerDiv = document.getElementById("offers");
    const eventDiv = document.getElementById("events");
    const scheduleDiv = document.getElementById("schedule");
    const offerButton = document.getElementById("offerButt");
    const eventButton = document.getElementById("eventButt");
    const scheduleButton = document.getElementById("scheduleButt");
    if (eventDiv.style.display === "none") {
        eventDiv.style.display = "block";
        offerDiv.style.display = "none";
        scheduleDiv.style.display = "none";
        offerButton.style.backgroundColor = "transparent";
        eventButton.style.backgroundColor = "darkgrey";
        scheduleButton.style.backgroundColor = "transparent";
    }
}
function showSchedule() {
    const offerDiv = document.getElementById("offers");
    const eventDiv = document.getElementById("events");
    const scheduleDiv = document.getElementById("schedule");
    const offerButton = document.getElementById("offerButt");
    const eventButton = document.getElementById("eventButt");
    const scheduleButton = document.getElementById("scheduleButt");
    if (scheduleDiv.style.display === "none") {
        eventDiv.style.display = "none";
        offerDiv.style.display = "none";
        scheduleDiv.style.display = "block";
        offerButton.style.backgroundColor = "transparent";
        eventButton.style.backgroundColor = "transparent";
        scheduleButton.style.backgroundColor = "darkgrey";
    }
}
