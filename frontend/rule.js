function rule(){
  //TODO:fix the rule pop-up window, the content is not right
  if (!document.querySelector('.modal')) {
    const modalContainer = document.getElementById('modalContainer');
    const modal = document.createElement('div');
    modal.classList.add('modal');
    modal.innerHTML = `
        <div class="modal-content">
            <span class="close" onclick="closeRules()">&times;</span>
            <h2>Game Rules</h2>
            <p>The game is simple:</p>
            <ul>
                <li>Guess a 3-digit number.</li>
                <li>If your guess has the right digit in the right place, you will see a clue.</li>
                <li>If your guess is wrong, try again until you find the right number.</li>
            </ul>
        </div>
    `;
    modalContainer.appendChild(modal);

    // Adding CSS directly via JavaScript
    const styles = `
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }
        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 50%; /* Adjust width as necessary */
        }
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }
        .close:hover,
        .close:focus {
            color: black;
        }
    `;
    const styleSheet = document.createElement('style');
    styleSheet.type = 'text/css';
    styleSheet.innerText = styles;
    document.head.appendChild(styleSheet);
}

document.querySelector('.modal').style.display = 'block';
}

function closeRules() {
document.querySelector('.modal').style.display = 'none';
}

// Optionally, add a listener to close the modal if the user clicks outside of it
window.onclick = function(event) {
const modal = document.querySelector('.modal');
if (event.target == modal) {
    closeRules();
}
}
