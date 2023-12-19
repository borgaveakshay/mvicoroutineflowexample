This application contains following key details

*Architecture*

- The underlying architecture is implemented in MVI (Model - View - Intent) pattern
    - There are three major components define under this architecture
      - UiEvent
        - The UiEvent components caters to all the interactions we would receive from the user.
      - UiState
        - The UiState defines the state of the screen at any given point of time and it will change its state depending upon the user interactions.
      - UiEffect
        - The UiEffect caters to all the side effect that a screen would have, for example acting on any api failures and showing a correct message to the user.
- For dependency injection hilt library is used.
- In App for UI Jetpack compose is used along with Jetpack compose navigation library.
- For the background tasks Kotlin Coroutines with flow is used as it is better integrated with the jetpack compose.
