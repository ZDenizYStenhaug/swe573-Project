function showOffers() {
    const offerDiv = document.getElementById("offers");
    const eventDiv = document.getElementById("events");
    const offerButton = document.getElementById("offerButt");
    const eventButton = document.getElementById("eventButt");
    if (offerDiv.style.display === 'none') {
        offerDiv.style.display = 'block';
        eventDiv.style.display = "none";
        offerButton.style.backgroundColor = "darkgrey";
        eventButton.style.backgroundColor = "transparent";
    }
}
function showEvents() {
    const offerDiv = document.getElementById("offers");
    const eventDiv = document.getElementById("events");
    const offerButton = document.getElementById("offerButt");
    const eventButton = document.getElementById("eventButt");
    if (eventDiv.style.display === "none") {
        eventDiv.style.display = "block";
        offerDiv.style.display = "none";
        offerButton.style.backgroundColor = "transparent";
        eventButton.style.backgroundColor = "darkgrey";
    }
}
