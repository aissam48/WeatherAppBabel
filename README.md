WeatherAppBabel:

-build by koltin and jetpack compose 

-for 3rd party i used https://www.worldweatheronline.com 

-for fetching data used ktor and kotlin serializable

-for dependency injecttion i used koin, easy integration library

-project respect mvvm pattern, contains on apiManager that has ktor configuration and all functions that i need, viewModels and screens also has common components and components
specific for screen

-project has two screen, first one has default list(casablanca, rabar, fes, tangier,marrakech) with info about weather, also 3 buttons first one for add new city and you will find bar that you have written city and country like "Casablanca, Morocco", second for refresh,
and third for fetch my current location, also search bar.
by click on item or city you will navigate to another screen that shows you more details about city weather also weather for next the 5 day, also has button to refresh info

for unit test i used JUnit, i have test 3 functions by mocking data, and expect results in success and error, DetailsViewModelTest.kt and MainViewModelTest.kt

<img width="317" alt="Screenshot 2025-05-12 at 09 30 55" src="https://github.com/user-attachments/assets/506544d5-48a0-4922-bf0e-41ec5e0695fb" />
<img width="317" alt="Screenshot 2025-05-12 at 09 33 40" src="https://github.com/user-attachments/assets/79473687-6055-4d7d-a29a-6f77ba0bf800" />
