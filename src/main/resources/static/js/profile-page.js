function showOffers() {
    const offerDiv = document.getElementById("offers");
    const eventDiv = document.getElementById("events");
    const scheduleDiv = document.getElementById("schedule");
    const ongoingDiv = document.getElementById("ongoing");
    const feedbackDiv = document.getElementById("feedback")
    const offerButton = document.getElementById("offerButt");
    const eventButton = document.getElementById("eventButt");
    const scheduleButton = document.getElementById("scheduleButt");
    const ongoingButt = document.getElementById("ongoingButt");
    const feedbackButt = document.getElementById("feedbackButt");
    if (offerDiv.style.display === 'none') {
        offerDiv.style.display = 'block';
        eventDiv.style.display = "none";
        scheduleDiv.style.display = "none";
        ongoingDiv.style.display = "none";
        feedbackDiv.style.display = "none";
        offerButton.style.backgroundColor = "darkgrey";
        eventButton.style.backgroundColor = "transparent";
        scheduleButton.style.backgroundColor = "transparent";
        ongoingButt.style.backgroundColor = "transparent";
        feedbackButt.style.backgroundColor = "transparent";
    }
}
function showEvents() {
    const offerDiv = document.getElementById("offers");
    const eventDiv = document.getElementById("events");
    const scheduleDiv = document.getElementById("schedule");
    const ongoingDiv = document.getElementById("ongoing");
    const feedbackDiv = document.getElementById("feedback")
    const offerButton = document.getElementById("offerButt");
    const eventButton = document.getElementById("eventButt");
    const scheduleButton = document.getElementById("scheduleButt");
    const ongoingButt = document.getElementById("ongoingButt");
    const feedbackButt = document.getElementById("feedbackButt");
    if (eventDiv.style.display === "none") {
        eventDiv.style.display = "block";
        offerDiv.style.display = "none";
        scheduleDiv.style.display = "none";
        ongoingDiv.style.display = "none";
        feedbackDiv.style.display = "none";
        offerButton.style.backgroundColor = "transparent";
        eventButton.style.backgroundColor = "darkgrey";
        scheduleButton.style.backgroundColor = "transparent";
        ongoingButt.style.backgroundColor = "transparent";
        feedbackButt.style.backgroundColor = "transparent";
    }
}
function showSchedule() {
    const offerDiv = document.getElementById("offers");
    const eventDiv = document.getElementById("events");
    const scheduleDiv = document.getElementById("schedule");
    const ongoingDiv = document.getElementById("ongoing");
    const feedbackDiv = document.getElementById("feedback")
    const offerButton = document.getElementById("offerButt");
    const eventButton = document.getElementById("eventButt");
    const scheduleButton = document.getElementById("scheduleButt");
    const ongoingButt = document.getElementById("ongoingButt");
    const feedbackButt = document.getElementById("feedbackButt");
    if (scheduleDiv.style.display === "none") {
        eventDiv.style.display = "none";
        offerDiv.style.display = "none";
        scheduleDiv.style.display = "block";
        ongoingDiv.style.display = "none";
        feedbackDiv.style.display = "none";
        offerButton.style.backgroundColor = "transparent";
        eventButton.style.backgroundColor = "transparent";
        scheduleButton.style.backgroundColor = "darkgrey";
        ongoingButt.style.backgroundColor = "transparent";
        feedbackButt.style.backgroundColor = "transparent";
    }
}
function showOngoing() {
    const offerDiv = document.getElementById("offers");
    const eventDiv = document.getElementById("events");
    const scheduleDiv = document.getElementById("schedule");
    const ongoingDiv = document.getElementById("ongoing");
    const feedbackDiv = document.getElementById("feedback")
    const offerButton = document.getElementById("offerButt");
    const eventButton = document.getElementById("eventButt");
    const scheduleButton = document.getElementById("scheduleButt");
    const ongoingButt = document.getElementById("ongoingButt");
    const feedbackButt = document.getElementById("feedbackButt");
    if (ongoingDiv.style.display === "none") {
        eventDiv.style.display = "none";
        offerDiv.style.display = "none";
        scheduleDiv.style.display = "none";
        ongoingDiv.style.display = "block";
        feedbackDiv.style.display = "none";
        offerButton.style.backgroundColor = "transparent";
        eventButton.style.backgroundColor = "transparent";
        scheduleButton.style.backgroundColor = "transparent";
        ongoingButt.style.backgroundColor = "darkgrey";
        feedbackButt.style.backgroundColor = "transparent";
    }
}
function showFeedbacks() {
    const offerDiv = document.getElementById("offers");
    const eventDiv = document.getElementById("events");
    const scheduleDiv = document.getElementById("schedule");
    const ongoingDiv = document.getElementById("ongoing");
    const feedbackDiv = document.getElementById("feedback")
    const offerButton = document.getElementById("offerButt");
    const eventButton = document.getElementById("eventButt");
    const scheduleButton = document.getElementById("scheduleButt");
    const ongoingButt = document.getElementById("ongoingButt");
    const feedbackButt = document.getElementById("feedbackButt");
    if (ongoingDiv.style.display === "none") {
        eventDiv.style.display = "none";
        offerDiv.style.display = "none";
        scheduleDiv.style.display = "none";
        ongoingDiv.style.display = "none";
        feedbackDiv.style.display = "block";
        offerButton.style.backgroundColor = "transparent";
        eventButton.style.backgroundColor = "transparent";
        scheduleButton.style.backgroundColor = "transparent";
        ongoingButt.style.backgroundColor = "transparent";
        feedbackButt.style.backgroundColor = "darkgrey";
    }
}
