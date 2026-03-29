# 🃏 Flip Memory Game

A polished **card-matching memory game** for Android built with Java. Flip cards, find matching pairs, and beat your high score across three difficulty levels!

---

## 📱 Screenshots


<img width="375" height="744" alt="image" src="https://github.com/user-attachments/assets/83b06180-5975-408a-b504-92639f3bd95d" />
<img width="410" height="757" alt="image" src="https://github.com/user-attachments/assets/7d2f3e94-555f-4df0-9000-1111a56ce688" />
<img width="354" height="720" alt="image" src="https://github.com/user-attachments/assets/3d860c7b-aed6-42b0-92a8-65fb860bde57" />




---

## 🎮 How to Play

| Step | Action |
|------|--------|
| 1 | Choose a difficulty level from the main menu |
| 2 | Tap any card to flip it face-up |
| 3 | Tap a second card to reveal it |
| 4 | ✅ Match → cards stay flipped and glow green |
| 5 | ❌ No match → cards flip back after a short delay |
| 6 | Find all pairs to win! |

---

## 🎯 Difficulty Levels

| Level | Grid | Pairs | Flip-back Delay |
|-------|------|-------|-----------------|
| 🌱 Beginner | 4 × 4 | 8 | 1200 ms |
| 🔥 Intermediate | 6 × 6 | 18 | 900 ms |
| 💀 Advanced | 8 × 8 | 32 | 600 ms |

---

## ✨ Features

- 🎴 Smooth **3D card flip animations**
- ⏱ **Live timer** tracking your speed
- 🎯 **Move counter** tracking every pair attempt
- 🏆 **High score system** saved per difficulty level
- ⭐ **Star rating** (1–3 stars) based on moves and time
- 🔄 **Restart button** to replay instantly
- 🎉 **Win dialog** showing moves, time and final score
- 🌙 **Dark themed UI** with gold accents

---

## 🏆 Scoring System

```
Score = BasePoints − MovePenalty − TimePenalty

BasePoints   = 1000 × (level index + 1)
MovePenalty  = (moves − pairs) × 5
TimePenalty  = elapsedSeconds × 2
```

| Stars | Condition |
|-------|-----------|
| ⭐⭐⭐ | 75%+ of max score |
| ⭐⭐ | 45%+ of max score |
| ⭐ | Below 45% |

---

## 🏗️ Project Structure

```
app/src/main/
├── java/com/example/flipmemorygame/
│   ├── MainActivity.java          → Level selection screen
│   ├── GameActivity.java          → Core game logic
│   ├── model/
│   │   ├── Card.java              → Card data model
│   │   └── GameLevel.java         → Difficulty level enum
│   ├── adapter/
│   │   └── CardAdapter.java       → RecyclerView adapter + flip animation
│   └── utils/
│       ├── CardDeck.java          → Shuffle and build card deck
│       ├── ScoreCalculator.java   → Score and star rating formula
│       └── SoundManager.java      → Sound effects wrapper
└── res/
    ├── layout/
    │   ├── activity_main.xml      → Level selection UI
    │   ├── activity_game.xml      → Game screen UI
    │   └── item_card.xml          → Individual card cell
    ├── anim/
    │   ├── card_flip_in.xml       → -90° → 0° Y-rotation
    │   └── card_flip_out.xml      → 0° → 90° Y-rotation
    └── values/
        ├── colors.xml
        ├── strings.xml
        ├── themes.xml
        └── dimens.xml
```

---

## 🔧 Tech Stack

| Technology | Usage |
|------------|-------|
| Java | Primary language |
| RecyclerView + GridLayoutManager | Card grid |
| CardView | Individual card faces |
| ObjectAnimator | 3D flip animation |
| Handler + Runnable | Game timer & mismatch delay |
| SharedPreferences | High score persistence |
| Material Components | UI theming & buttons |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 11+
- Android device or emulator running **API 24+**

### Installation

1. Clone the repo:
```bash
git clone https://github.com/sana-ash110/FlipMemoryGame.git
```

2. Open in Android Studio:
```
File → Open → select the FlipMemoryGame folder
```

3. Let Gradle sync finish

4. Run on emulator or device:
```
Run → Run 'app'  (Shift + F10)
```

---

## 🔊 Sound Effects (Optional)

To enable sounds, add these files to `app/src/main/res/raw/`:

| File | Triggered when |
|------|---------------|
| `flip.ogg` | Any card is tapped |
| `match.ogg` | A correct pair is found |
| `wrong.ogg` | Cards don't match |
| `win.ogg` | All pairs matched |

Free sounds available at [freesound.org](https://freesound.org)

---

## 🔮 Future Improvements

- [ ] Add sound effects
- [ ] Add best time tracking per level
- [ ] Add card themes (animals, fruits, flags)
- [ ] Add a local leaderboard using Room database
- [ ] Add haptic feedback on match/mismatch
- [ ] Landscape mode support

---

## 👩‍💻 Author

**Sana Ashraf**
- GitHub: [@sana-ash110](https://github.com/sana-ash110)
- LinkedIn: [sana-ashraf](https://www.linkedin.com/in/sana-ashraf-24a9b7302)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
