**GitHub** : https://github.com/justgoh/CW2025

**Compilation Instructions**

**Implemented and Working Properly:**

1. **Tetris Piece Movement**

- Left/Right movement (Arrow keys or A/D)

- Rotation (Up arrow or W)

- Soft drop (Down arrow or S)with score bonus (+1 per row)

- Hard drop (Spacebar) with
  distance-based scoring

2. **Game Mechanics**

- Line clearing with proper scoring system

- Collision detection for pieces and boundaries

- Game over detection when pieces stack to the top

3. **Enhanced Scoring System**

- Points awarded for soft drops (+1 per row)

- Points awarded for hard drops (based on distance dropped)

- Improved line clear bonuses:

  1 line: 50 points

  2 lines: 200 points

  3 lines: 450 points

  4 lines: 800 points

4. **Classic Mode**

- Standard Tetris gameplay

- Unlimited time

- Standard drop speed (500ms)

5. **Time Attack Mode**

- 2-minute countdown timer

- Faster initial drop speed (300ms)

- Game ends when timer reaches zero

- Timer color changes (green → orange → red as time runs low)

- Separate leaderboard from Classic mode

6. **Hold Piece System**

- Store current piece for later use (C or Shift key)

- Visual display of held piece in dedicated panel

- Can only hold once per piece placement

- Button state changes to indicate availability

- Prevents hold spam exploit

7. **Next Piece Preview**

- Shows next 3 upcoming pieces (original only showed 1)

- Real-time updates as pieces spawn

- Helps with strategic planning

- Rendered in dedicated preview panels

8. **Ghost Piece**

- Semi-transparent outline showing landing position

- Updates in real-time as piece moves/rotates

- Helps with precise placement

9. **Visual Themes**

- Default: Dark gradient theme

- Countryside: Nature-inspired background image

- Beach: Beach scene background image

- Tron: Futuristic neon theme background

- Themes apply to main menu and game screens

10. **Comprehensive Menu System**

- Main Menu: Classic, Time Attack, theme selection, leaderboard, exit

- How to Play Screen: Complete control instructions, scoring breakdown, feature explanations

- Theme Selection Menu: Visual previews of all themes

- Pause Menu: Resume, restart, main menu, quit options

- Game Over Screen: Play again, main menu options

11. **Score Display**

- Real-time score updates using JavaFX property binding

- High score display for current mode

- Mode indicator (Classic/Time Attack)

- Score animations for line clears

12. **Leaderboard System**

- Separate leaderboards for Classic and Time Attack modes

- Top 10 scores saved persistently to text files

- Automatic sorting in descending order

- Scores persist between application restarts

- Toggle between mode leaderboards

- Formatted display with rankings

13. **Enhanced Keyboard Controls**

- Multiple key bindings (Arrow keys + WASD)

- New game with N key

- Pause/Resume with P key

- Hold piece with C or Shift

- All controls responsive and intuitive

14. **Input State Management**

- Input blocked during pause

- Input blocked during game over (except N for new game, P for pause)

- Proper event consumption to prevent multiple triggers

15. **Score Notifications**

- Animated pop-up notifications for line clears

- Shows score bonus earned

- Smooth fade-out and translate animations

- Non-intrusive placement

16. **Reflection Effects**

- Visual polish with reflection effects on UI elements

- Configurable opacity and positioning through constants

17. **Responsive Layout**

- Properly sized game board

- Sidebars with all necessary information

- Clean, modern button designs

- Proper spacing and alignment

**Implemented but Not Working Properly:**

**None**

All implemented features are functioning as expected. Extensive testing has been conducted for:

- Piece movement and rotation in all scenarios

- Wall kicks and boundary detection

- Collision detection accuracy

- Line clearing logic

- Score calculation correctness

- Timer functionality and pause behavior

- Theme switching reliability

- Leaderboard file persistence

- Hold piece state management

- Ghost piece positioning accuracy

**Features Not Implemented:**

1. **Progressive Difficulty in Classic Mode**

**Reason**: Time constraints prevented full implementation of speed increase based on level/score. The current
implementation has a fixed drop speed for Classic mode (500ms) and Time Attack mode (300ms).

2. **Sound Effects and Background Music**

**Reason**: Focus was prioritized on core gameplay mechanics and visual features. Audio system would require additional
dependencies (JavaFX Media) and significant implementation time.

3. **Multiplayer Mode**

**Reason**: Scope limitation. Multiplayer would require network implementation, significantly increasing project
complexity
beyond assignment scope.

4. **Game Statistics Dashboard**

**Reason**: Focus was on leaderboard functionality. Detailed statistics would require database or complex file structure
beyond simple text files.

5. **Replay System**

**Reason**: Would require recording all inputs with timestamps and playback mechanism, adding significant complexity.

6. **T- Spin**

**Reason**: T-spin detection requires complex pattern recognition algorithms.

**New Java Classes:**

1. **GameTimeline.java**
    - **Purpose:** Manages the game's automatic piece dropping timeline
    - **Location:** src/main/java/com/comp2042/controller/
    - **Key Responsibilities:**
        - Encapsulates JavaFX Timeline for game loop control
        - Provides start, stop, pause, and resume functionality
        - Handles adjustable tick rates for different game speeds (Classic: 500ms, Time Attack: 300ms)
        - Ensures proper resource cleanup to prevent memory leaks
        - Tracks running state independently of Timeline object
    - **Why Created:** Original code had Timeline directly in GuiController with no encapsulation, making it difficult
      to manage game loop state and causing memory leaks on restart


2. **GameInputHandler.java**
    - **Purpose:** Centralizes keyboard input processing with clean callback architecture
    - **Location:** src/main/java/com/comp2042/controller/
    - **Key Responsibilities:**
        - Routes key presses to appropriate game actions
        - Respects game state (pause, game over) before processing input
        - Implements callback pattern for action handlers (pause, new game, hard drop, hold)
        - Supports multiple key bindings (arrows + WASD)
        - Separates input handling from UI controller logic
    - **Why Created:** Original GuiController had messy anonymous EventHandler with all input logic inline. Extracted
      for better separation of concerns and testability


3. **HighScoreManager.java**
    - **Purpose:** Manages persistent high score storage and retrieval
    - **Location:** src/main/java/com/comp2042/model/
    - **Key Responsibilities:**
        - Loads and saves scores to text files (classic_leaderboard.txt, timeattack_leaderboard.txt)
        - Maintains separate leaderboards for each game mode
        - Automatically sorts scores in descending order
        - Provides top N scores retrieval
        - Handles file I/O errors gracefully
    - **Why Created:** Original code had no score persistence. Needed for leaderboard feature requirement


4. **HighScoreManager.GameMode (enum)**
    - **Purpose:** Enum defining available game modes
    - **Location:** Inside HighScoreManager.java
    - **Values:** CLASSIC, TIME_ATTACK
    - **Why Created:** Type-safe way to distinguish between game modes for score management


5. **TetrisColor.java**
    - **Purpose:** Enum for Tetris piece colors with centralized color definitions
    - **Location:** src/main/java/com/comp2042/model/
    - **Key Responsibilities:**
        - Maps color codes (0-7) to JavaFX Paint objects
        - Provides getColorByCode() method for color lookup
        - Centralizes color definitions for consistency across renderers
        - Associates colors with piece types (I=Cyan, T=Purple, etc.)
    - **Why Created:** Original code had color mapping scattered in GuiController switch statement. Extracted to enum
      for better organization and reusability


6. **Theme.java**
    - **Purpose:** Enum for UI theme definitions
    - **Location:** src/main/java/com/comp2042/model/
    - **Key Responsibilities:**
        - Defines available themes (DEFAULT, COUNTRYSIDE, BEACH, TRON)
        - Stores theme resources (CSS gradient strings or image filenames)
        - Distinguishes between gradient and image-based themes via ThemeType enum
    - **Why Created:** New feature for visual customization. Enum provides type-safe theme selection


7. **Theme.ThemeType (enum)**
    - **Purpose:** Distinguishes gradient vs image themes
    - **Location:** Inside Theme.java
    - **Values:** GRADIENT, IMAGE
    - **Why Created:** Theme manager needs to apply themes differently based on type


8. **UIState.java**
    - **Purpose:** Enum for UI state machine management
    - **Location:** src/main/java/com/comp2042/model/
    - **Values:** HOME, HOW_TO_PLAY, THEMES, GAME, PAUSED, GAME_OVER, LEADERBOARD
    - **Why Created:** Type-safe state management for ViewManager transitions


9. **ViewManager.java**
    - **Purpose:** Manages UI screen visibility and transitions
    - **Location:** src/main/java/com/comp2042/view/
    - **Key Responsibilities:**
        - Controls visibility of all menus and game screens
        - Ensures only appropriate screens are visible at any time
        - Provides methods: showHome(), showGame(), showPause(), showLeaderboard(), etc.
        - Null-safe visibility toggling for all components
        - Centralizes view state management
    - **Why Created:** Original code had no menu system. With multiple screens added, needed centralized manager to
      coordinate visibility


10. **PieceRenderer.java**
    - **Purpose:** Handles visual rendering of Tetris pieces
    - **Location:** src/main/java/com/comp2042/view/
    - **Key Responsibilities:**
        - Creates styled Rectangle objects for pieces
        - Applies colors and styling (rounded corners, fills)
        - Renders pieces in preview panels (next pieces, hold piece)
        - Creates ghost piece visuals with transparency
        - Initializes preview panel grids
    - **Why Created:** Extracted rendering logic from GuiController for separation of concerns. Reusable for multiple
      preview panels


11. **ThemeManager.java**
    - **Purpose:** Applies visual themes to UI components
    - **Location:** src/main/java/com/comp2042/view/
    - **Key Responsibilities:**
        - Applies gradient themes via CSS inline styles
        - Applies image themes via BackgroundImage
        - Updates UI component backgrounds
        - Handles theme switching dynamically
        - Works with Theme enum for theme definitions
    - **Why Created:** New feature for visual customization. Encapsulates theme application logic


12. **LeaderboardView.java**
    - **Purpose:** Displays leaderboard information in UI
    - **Location:** src/main/java/com/comp2042/view/
    - **Key Responsibilities:**
        - Formats leaderboard entries as Labels
        - Creates visual ranking display (1st, 2nd, 3rd, etc.)
        - Updates leaderboard UI when switching modes
        - Handles empty leaderboard display
        - Applies styling to entries
    - **Why Created:** New leaderboard feature needed dedicated view component for display


13. **GameConstants.java**
    - **Purpose:** Game-related constant values
    - **Location:** src/main/java/com/comp2042/constants/
    - **Key Constants:**
        - BRICK_SIZE = 20 (size of each brick cell in pixels)
        - INVISIBLE_ROWS = 2 (rows hidden at top for spawning)
        - NEXT_PIECE_PREVIEW_COUNT = 3 (number of next pieces to show)
        - MAX_LEADERBOARD_ENTRIES = 10 (top scores to save)
        - GHOST_OPACITY, GHOST_STROKE_WIDTH (ghost piece appearance)
        - BRICK_CORNER_RADIUS (rounded corners on bricks)
        - Timer threshold constants for color changes
    - **Why Created:** Centralize magic numbers for maintainability. Original had hardcoded values scattered throughout


14. **UIConstants.java**
    - **Purpose:** UI layout and spacing constants
    - **Location:** src/main/java/com/comp2042/constants/
    - **Key Constants:**
        - PREVIEW_PANEL_PADDING = 5
        - LEADERBOARD_ENTRY_SPACING = 10
        - LEADERBOARD_ENTRY_WIDTH = 300
        - LEADERBOARD_RANK_WIDTH = 30
        - GAME_BOARD_HGAP = 0, GAME_BOARD_VGAP = 0
    - **Why Created:** Centralize UI measurements for consistent spacing


15. **AnimationConfig.java**
    - **Purpose:** Animation and timing constants
    - **Location:** src/main/java/com/comp2042/constants/
    - **Key Constants:**
        - DEFAULT_GAME_TICK_DELAY_MS = 400 (base game speed)
        - REFLECTION_FRACTION = 0.8 (reflection effect height)
        - REFLECTION_TOP_OPACITY = 0.9 (reflection transparency)
        - REFLECTION_TOP_OFFSET = -12 (reflection positioning)
    - **Why Created:** Centralize animation parameters. Original had some hardcoded in GuiController

**Modified Java Classes:**

1. **Main.java**
    - **Location:** src/main/java/com/comp2042/view (moved from com.comp2042)
    - **Modifications:**
        - Updated window size to 1080x720 for larger UI
        - Same basic structure maintained
    - **Reason:** Need larger window for enhanced UI with menus and sidebars


2. **GuiController.java**
    - **Location:** src/main/java/com/comp2042/controller/ (moved from com.comp2042)
    - **Modifications:**
        - Massive expansion of FXML components
        - Extracted Timeline management: Now uses GameTimeline class instead of inline Timeline
        - Extracted input handling: Now uses GameInputHandler instead of anonymous EventHandler
        - Added dependency injection setters: For PieceRenderer, ThemeManager, ViewManager, LeaderboardView,
          GameController
        - Implemented score binding: scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"))
        - Added next piece preview system: Initialize and update 3 preview panels
        - Added hold piece system: Initialize hold panel, handle hold action, update display
        - Added ghost piece rendering: Calculate and render ghost piece position
        - Added theme support
        - Added all menu navigation methods
        - Added game mode support: Methods for Classic/Time Attack mode, timer display
        - Added leaderboard methods: Show/hide leaderboard for different modes
        - Improved piece rendering: Proper rectangle recreation on rotation, better positioning
        - Added hard drop functionality: New method for instant piece drop
        - Enhanced refreshBrick: Now handles piece shape changes (rotation), updates ghost piece, updates next pieces
        - Better state management: Uses ViewManager for screen transitions
        - Null safety: Added null checks throughout for FXML components
    - **Reason:** Original was minimal skeleton. Needed complete UI management for all new features


3. **GameController.java**
    - **Location:** src/main/java/com/comp2042/controller/ (moved from com.comp2042)
    - **Modifications:**
        - Added HighScoreManager:** Load/save high scores persistently
        - Added game mode system
        - Added Time Attack timer: Timeline for countdown, with pause/resume
        - Added hold piece system
        - Added hard drop:** onHardDrop() method with distance scoring
        - Added ghost piece checking:
        - Added next pieces retrieval
        - Added leaderboard access
        - Added pause/resume
        - Added proper game over handling: handleGameOver() with score saving
        - Enhanced createNewGame: Reset hold piece, update high score label, handle timer based on mode
    - **Reason:** Original was minimal. Needed to become central game logic coordinator for all new features


4. **Board.java (interface)**
    - **Location:** src/main/java/com/comp2042/model/ (moved from com.comp2042)
    - **Modifications:**
        - Added: ClearRow hardDropBrick() - For instant drop with scoring
        - Added: void spawnBrick(Brick brick) - For hold piece swapping
        - Added: boolean checkCollision() - For validating hold swaps
        - Added: Brick getCurrentBrick() - Get current brick reference
        - Added: BrickGenerator getBrickGenerator() - Access generator for preview
    - **Reason:** Original interface lacked methods needed for hold piece and hard drop features


5. **SimpleBoard.java**
    - **Location:** src/main/java/com/comp2042/model/ (moved from com.comp2042)
    - **Modifications:**
        - Added hardDropBrick() method
        - Added spawnBrick(Brick brick) method: Set specific brick (for hold piece)
        - Added checkCollision() method: Check if current position is valid
        - Added getCurrentBrick() method: Return current brick reference
        - Added getBrickGenerator() method: Expose generator for next piece preview
        - Modified createNewBrick(): Better spawn positioning, configurable
        - Enhanced collision detection: More robust boundary checking
    - **Reason:** Added new features for the game


6. **BrickRotator.java**
    - **Location:** src/main/java/com/comp2042/model/ (moved from com.comp2042)
    - **Modifications:**
        - Removed: setBrickImmediately() method was unused
        - Improved documentation
    - **Reason:** Cleanup of unused methods


7. **Score.java**
    - **Location:** src/main/java/com/comp2042/model/ (moved from com.comp2042)
    - **Modifications:**
        - Added bounds checking in add() : Prevent negative scores
        - Added `getScore() method: Direct access to current score value
    - **Reason:** Minor enhancements for safety and usability


8. **MatrixOperations.java**
    - **Location:** src/main/java/com/comp2042/model/ (moved from com.comp2042)
    - **Modifications:**
        - Enhanced checkRemoving() scoring
    - **Reason:** Clarify scoring system, ensure proper Tetris scoring


9. **InputEventListener.java (interface)**
    - **Location:** src/main/java/com/comp2042/view/ (moved from com.comp2042)
    - **Modifications:**
        - Added: DownData onHardDrop(ViewData brick) - Hard drop handling
        - Added: boolean onGhostCheck(ViewData brick) - Ghost piece collision check
        - Added: ViewData getCurrentBrick() - Get current piece state
        - Added: List<int[][]> getNextPieces(int count) - Get upcoming pieces for preview
        - Added: boolean onHoldEvent() - Hold piece handling
        - Added: int[][] getHoldPiece() - Get held piece data
    - **Reason:** Interface needed extension for new features (hold, hard drop, preview, ghost)


10. **ViewData.java**
    - **Location:** src/main/java/com/comp2042/view/ (moved from com.comp2042)
    - **Modifications:**
        - Added: copyWithPosition(int x, int y) method - Create copy with new position (for ghost piece calculation)
    - **Reason:** Ghost piece feature needed way to test positions without modifying original


11. **NotificationPanel.java**
    -**Location:** src/main/java/com/comp2042/view/ (moved from com.comp2042)
    - **Modifications:**
        - Enhanced styling: Better positioning, improved glow effect
        - Improved animation timing: Smoother transitions
        - Better cleanup: Properly removes self from parent
    - **Reason:** Minor polish improvements


12. **GameOverPanel.java**
    -**Location:** none (deleted)
    - **Reason:** Moved to FXML-based design for consistency with other panels


13. **Brick Classes** (Brick.java and subclasses)
    -**Location:** src/main/java/com/comp2042/logic/bricks (moved from com.comp2042)
    - **Note:**
        - These existed in original (IBrick, JBrick, LBrick, OBrick, SBrick, TBrick, ZBrick)
    - **Modifications:**
        - Added color code constants to each brick type (1-7)
        - Minor adjustments to rotation matrices for better wall kicks
    - **Reason:** Support TetrisColor enum with consistent color coding


14. **BrickGenerator.java**
    -**Location:** src/main/java/com/comp2042/logic/bricks/ (moved from com.comp2042)
    - **Modifications:**
        - Added List<Brick> getNextBricks(int count) for brick preview feature
    - **Reason:** Support 3-piece preview feature


15. **RandomBrickGenerator.java**
    -**Location:** src/main/java/com/comp2042/logic/bricks (moved from com.comp2042)
    - **Modifications:**
        - Added getNextBricks(int count) method: Return list of upcoming pieces for preview
        - Implemented "bag" randomization: Fair piece distribution (7-bag system)
    - **Reason:** Have more fair randomization of bricks and brick preview feature

**Unexpected Problems:**

1. **JavaFX Timeline Memory Leaks**
   Problem: Initial implementation caused memory leaks when repeatedly starting/stopping games. The Timeline was not
   being properly cleaned up, leading to multiple Timeline instances running simultaneously.
   Fix:
    - Created dedicated GameTimeline class to encapsulate Timeline lifecycle
    - Ensured only one Timeline instance exists at a time
    - Always call stop() before creating new Timeline in start()


2. **Integer Property Warnings in IntelliJ**
   Problem: IntelliJ suggested replacing .get(0) with .getFirst() everywhere.
   Fix: Updated all instances


3. **Timer Not Pausing Correctly in Time Attack**
   Problem: Timer continued running when game was paused, causing unfair gameplay in Time Attack mode.
   Fix:
    - Added pause/resume methods to GameController for timer
    - Connected GuiController pause button to call gameController.pauseGame()
    - Added mode flag check to only pause timer in Time Attack mode
    - Ensured timer state syncs with game pause state