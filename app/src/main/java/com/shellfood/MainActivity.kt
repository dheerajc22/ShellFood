package com.shellfood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.shellfood.ui.theme.ShellFoodTheme
import com.shellfood.components.FoodCard
import com.shellfood.components.CuisineItem
import com.shellfood.components.OfferBanner
import com.shellfood.components.BottomNavigationBar
import com.shellfood.screens.LoginScreen
import com.shellfood.screens.CartScreen
import com.shellfood.data.FavoriteDataStore
import com.shellfood.data.FoodItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.shellfood.screens.CheckoutScreen
import com.shellfood.screens.OrderConfirmationScreen
import com.shellfood.data.Order
import com.shellfood.screens.MyOrdersScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shellfood.data.local.ShellFoodDatabase
import com.shellfood.data.repository.CartRepository
import com.shellfood.viewmodel.CartViewModel
import com.shellfood.viewmodel.CartViewModelFactory
import com.shellfood.viewmodel.OrderViewModel
import com.shellfood.viewmodel.OrderViewModelFactory
import com.shellfood.data.repository.OrderRepository
import android.widget.Toast
import java.util.UUID
import kotlin.math.roundToInt

private val ShellOrange = Color(0xFFFF6B00)
private val ShellDark = Color(0xFF252525)
private val ShellCream = Color(0xFFFFF7F0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShellFoodTheme {
                ShellFoodApp()
            }
        }
    }
    @Composable
    fun ShellFoodApp() {
        var currentScreen by remember {
            mutableStateOf("login")
        }
        var phoneNumber by remember {
            mutableStateOf("")
        }
        var generatedOtp by remember {
            mutableStateOf("")
        }
        var currentOrder by remember {
            mutableStateOf<Order?>(null)
        }
        // Initialize DataStore
        val context = LocalContext.current
        val favoriteDataStore = remember(context) {
            FavoriteDataStore(context)
        }
        // Initialize Room Database
        val database = remember(context) {
            ShellFoodDatabase.getDatabase(context)
        }
// Initialize Cart Repository
        val cartRepository = remember(database) {
            CartRepository(database.cartDao())
        }
// Initialize Cart ViewModel
        val cartViewModel: CartViewModel = viewModel(
            factory = CartViewModelFactory(cartRepository)
        )
        val orderRepository = remember(database) {
            OrderRepository(database)
        }
        val orderViewModel: OrderViewModel = viewModel(
            factory = OrderViewModelFactory(orderRepository)
        )
        // Observe saved favorite IDs
        val favoriteIds by favoriteDataStore.favoriteIds.collectAsState(
            initial = emptySet()
        )
        // Coroutine scope
        val coroutineScope = rememberCoroutineScope()
        // Food catalogue
        val foodItems = listOf(
            FoodItem(
                id = "burger_001",
                name = "Classic Burger",
                description = "Juicy burger with cheese",
                price = 149,
                emoji = "🍔",
                isVeg = false
            ),
            FoodItem(
                id = "pizza_001",
                name = "Veg Pizza",
                description = "Loaded with fresh vegetables",
                price = 199,
                emoji = "🍕",
                isVeg = true
            ),
            FoodItem(
                id = "biryani_001",
                name = "Chicken Biryani",
                description = "Aromatic dum biryani",
                price = 249,
                emoji = "🍛",
                isVeg = false
            ),
            FoodItem(
                id = "wrap_001",
                name = "Paneer Wrap",
                description = "Paneer with spicy sauces",
                price = 129,
                emoji = "🌯",
                isVeg = true
            )
        )
        // Convert saved IDs into food objects
        val favoriteFoods = foodItems.filter { food ->
            food.id in favoriteIds
        }
        // Bottom navigation selected tab
        var selectedTab by remember {
            mutableStateOf("Home")
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = ShellCream
        ) {
            when (currentScreen) {
                "login" -> LoginScreen(
                    onGetStarted = {
                        currentScreen = "phone"
                    }
                )
                "phone" -> PhoneNumberScreen(
                    onBackClick = {
                        currentScreen = "login"
                    },
                    onContinue = { enteredNumber ->
                        phoneNumber = enteredNumber
                        generatedOtp = generateOtp()
                        currentScreen = "otp"
                    }
                )
                "otp" -> OtpScreen(
                    phoneNumber = phoneNumber,
                    generatedOtp = generatedOtp,
                    onBackClick = {
                        currentScreen = "phone"
                    },
                    onResendOtp = {
                        generatedOtp = generateOtp()
                    },
                    onVerificationSuccess = {
                        currentScreen = "success"
                    }
                )
                "success" -> LoginSuccessfulScreen(
                    onEnterApplication = {
                        currentScreen = "home"
                    }
                )
                "order-confirmation" -> {
                    currentOrder?.let { order ->
                        OrderConfirmationScreen(
                            order = order,
                            onContinueShopping = {
                                currentOrder = null
                                selectedTab = "Home"
                                currentScreen = "home"
                            }
                        )
                    }
                }
                "home" -> HomeScreen(
                    favoriteFoods = favoriteFoods,
                    orderViewModel = orderViewModel,
                    cartViewModel = cartViewModel,

                    onFavoriteClick = { food ->
                        coroutineScope.launch {
                            if (food.id in favoriteIds) {
                                favoriteDataStore.removeFavorite(food.id)
                            } else {
                                favoriteDataStore.addFavorite(food.id)
                            }
                        }
                    },

                    selectedTab = selectedTab,

                    onTabSelected = {
                        selectedTab = it
                    },

                    onOrderPlaced = { order ->
                        currentOrder = order
                        currentScreen = "order-confirmation"
                    }
                )
            }
        }
    }
    // OTP GENERATION
    private fun generateOtp(): String {
        return (100000..999999)
            .random()
            .toString()
    }
    // PHONE NUMBER SCREEN
    @Composable
    fun PhoneNumberScreen(
        onBackClick: () -> Unit,
        onContinue: (String) -> Unit
    ) {
        var phoneNumber by remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ShellCream)
                .padding(24.dp)
        ) {
            TextButton(onClick = onBackClick) {
                Text(
                    text = "← Back",
                    color = ShellOrange,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(35.dp))
            Text(
                text = "🍔",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "What's your\nphone number?",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ShellDark
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "We'll send you a one-time password to verify your number.",
                fontSize = 15.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { enteredNumber ->
                    if (
                        enteredNumber.length <= 10 &&
                        enteredNumber.all { it.isDigit() }
                    ) {
                        phoneNumber = enteredNumber
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Phone Number")
                },
                leadingIcon = {
                    Text(
                        text = "+91",
                        fontWeight = FontWeight.Bold,
                        color = ShellDark
                    )
                },
                placeholder = {
                    Text("Enter 10-digit number")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ShellOrange,
                    focusedLabelColor = ShellOrange
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    onContinue(phoneNumber)
                },
                enabled = phoneNumber.length == 10,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ShellOrange
                )
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    // OTP VERIFICATION SCREEN
    @Composable
    fun OtpScreen(
        phoneNumber: String,
        generatedOtp: String,
        onBackClick: () -> Unit,
        onResendOtp: () -> Unit,
        onVerificationSuccess: () -> Unit
    ) {
        var otp by remember(generatedOtp) {
            mutableStateOf("")
        }
        var remainingSeconds by remember(generatedOtp) {
            mutableStateOf(30)
        }
        var errorMessage by remember {
            mutableStateOf("")
        }
        LaunchedEffect(generatedOtp) {
            remainingSeconds = 30
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ShellCream)
                .padding(24.dp)
        ) {
            TextButton(onClick = onBackClick) {
                Text(
                    text = "← Back",
                    color = ShellOrange,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "🔐",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Verify your\nphone number",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ShellDark
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Enter the 6-digit OTP sent to +91 $phoneNumber",
                fontSize = 15.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = otp,
                onValueChange = { enteredOtp ->
                    if (
                        enteredOtp.length <= 6 &&
                        enteredOtp.all { it.isDigit() }
                    ) {
                        otp = enteredOtp
                        errorMessage = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Enter OTP")
                },
                placeholder = {
                    Text("6-digit code")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ShellOrange,
                    focusedLabelColor = ShellOrange
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (remainingSeconds > 0) {
                    "OTP expires in 00:%02d".format(remainingSeconds)
                } else {
                    "OTP expired"
                },
                color = if (remainingSeconds > 0) {
                    ShellOrange
                } else {
                    Color.Red
                },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = " OTP: $generatedOtp",
                color = Color.Gray,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (remainingSeconds == 0) {
                TextButton(
                    onClick = {
                        otp = ""
                        errorMessage = ""
                        onResendOtp()
                    }
                ) {
                    Text(
                        text = "Resend OTP",
                        color = ShellOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    when {
                        remainingSeconds == 0 -> {
                            errorMessage =
                                "OTP has expired. Please resend OTP."
                        }
                        otp == generatedOtp -> {
                            onVerificationSuccess()
                        }
                        else -> {
                            errorMessage =
                                "Invalid OTP. Please try again."
                        }
                    }
                },
                enabled = otp.length == 6,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ShellOrange
                )
            ) {
                Text(
                    text = "Verify OTP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    // LOGIN SUCCESSFUL SCREEN
    @Composable
    fun LoginSuccessfulScreen(
        onEnterApplication: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ShellCream)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎉",
                fontSize = 80.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Login Successful!",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ShellDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Welcome to Shell FoOoOoOd.\nLet's satisfy your cravings!",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onEnterApplication,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ShellOrange
                )
            ) {
                Text(
                    text = "Enter Application",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    // HOME SCREEN
    @Composable
    fun HomeScreen(
        favoriteFoods: List<FoodItem>,
        onFavoriteClick: (FoodItem) -> Unit,
        selectedTab: String,
        onTabSelected: (String) -> Unit,
        cartViewModel: CartViewModel,
        orderViewModel: OrderViewModel,
        onOrderPlaced: (Order) -> Unit
    ) {
        val context = LocalContext.current

        var searchQuery by remember {
            mutableStateOf("")
        }
        var selectedFoodType by remember {
            mutableStateOf("All")
        }
        val foodItems = listOf(
            FoodItem(
                "burger_001",
                "Classic Burger",
                "Juicy burger with cheese",
                149,
                "🍔",
                false
            ),
            FoodItem(
                "pizza_001",
                "Veg Pizza",
                "Loaded with fresh vegetables",
                199,
                "🍕",
                true
            ),
            FoodItem(
                "biryani_001",
                "Chicken Biryani",
                "Aromatic dum biryani",
                249,
                "🍛",
                false
            ),
            FoodItem(
                "wrap_001",
                "Paneer Wrap",
                "Paneer with spicy sauces",
                129,
                "🌯",
                true
            )
        )
        val filteredFoods = foodItems.filter { food ->
            val matchesSearch =
                food.name.contains(searchQuery, ignoreCase = true)
            val matchesType = when (selectedFoodType) {
                "Veg" -> food.isVeg
                "Non-Veg" -> !food.isVeg
                else -> true
            }
            matchesSearch && matchesType
        }
        Scaffold(
            containerColor = Color(0xFFFFF8F0),
            bottomBar = {
                BottomNavigationBar(
                    selectedItem = selectedTab,
                    onItemSelected = onTabSelected
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF8F0))
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (selectedTab) {
                    // HOME TAB
                    "Home" -> {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "DELIVER TO",
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "📍 Your Location",
                                        color = Color(0xFFE86A16),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "👤",
                                    fontSize = 28.sp
                                )
                            }
                        }
                        item {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = {
                                    searchQuery = it
                                },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text("Search food or restaurants")
                                },
                                leadingIcon = {
                                    Text("🔍")
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFE86A16),
                                    unfocusedBorderColor = Color.LightGray
                                )
                            )
                        }
                        item {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("All", "Veg", "Non-Veg").forEach { type ->

                                    FilterChip(
                                        selected = selectedFoodType == type,
                                        onClick = {
                                            selectedFoodType = type
                                        },
                                        label = {
                                            Text(type)
                                        }
                                    )
                                }
                            }
                        }
                        item {
                            OfferBanner()
                        }
                        item {
                            Text(
                                text = "Explore Cuisines",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF292929)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                CuisineItem("🍕", "Pizza")
                                CuisineItem("🍔", "Burgers")
                                CuisineItem("🍛", "Biryani")
                                CuisineItem("🍜", "Noodles")
                            }
                        }
                        item {
                            Text(
                                text = "Popular Near You",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF292929)
                            )
                        }
                        if (filteredFoods.isEmpty()) {
                            item {
                                Text(
                                    text = "No food items found.",
                                    color = Color.Gray
                                )
                            }
                        } else {
                            items(filteredFoods) { food ->
                                FoodCard(
                                    food = food,

                                    isFavorite = favoriteFoods.any {
                                        it.id == food.id
                                    },
                                    onFavoriteClick = {
                                        onFavoriteClick(food)
                                    },
                                    onAddToCart = {
                                        cartViewModel.addToCart(food)
                                    }
                                )
                            }
                        }
                    }
                    // CART TAB
                    "Cart" -> {
                        item {
                            CartScreen(
                                cartViewModel = cartViewModel,
                                onCheckoutClick = {
                                    onTabSelected("Checkout")
                                }
                            )
                        }
                    }
                    // CHECKOUT TAB

                    "Checkout" -> {
                        item {
                            CheckoutScreen(
                                cartViewModel = cartViewModel,
                                onBackClick = {
                                    onTabSelected("Cart")
                                },
                                onPlaceOrder = { paymentMethod ->

                                    val cartItems = cartViewModel.cartItems.value

                                    if (cartItems.isNotEmpty()) {

                                        val subtotal = cartItems.sumOf {
                                            it.totalPrice
                                        }

                                        val deliveryFee = 30
                                        val tax = (subtotal * 0.05).roundToInt()
                                        val totalAmount = subtotal + deliveryFee + tax

                                        val order = Order(
                                            orderId = UUID.randomUUID()
                                                .toString()
                                                .take(8)
                                                .uppercase(),
                                            items = cartItems,
                                            subtotal = subtotal,
                                            deliveryFee = deliveryFee,
                                            tax = tax,
                                            totalAmount = totalAmount,
                                            paymentMethod = paymentMethod,
                                            orderStatus = "Confirmed"
                                        )

                                        orderViewModel.placeOrder(
                                            order = order,
                                            onSuccess = {
                                                cartViewModel.clearCart()
                                                onOrderPlaced(order)
                                            },
                                                onError = { message ->
                                                    Toast.makeText(
                                                        context,
                                                        message,
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        )
                                    }
                                }
                            )
                        }
                    }
                    // FAVORITES TAB
                    "Favorites" -> {
                        item {
                            Text(
                                text = "My Favorites ❤️",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF292929)
                            )
                            Text(
                                text = "Your saved food items",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        if (favoriteFoods.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 60.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "💔",
                                        fontSize = 55.sp
                                    )
                                    Spacer(
                                        modifier = Modifier.height(12.dp)
                                    )
                                    Text(
                                        text = "No Favorites Yet!",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Add your favorite food from Home.",
                                        color = Color.Gray
                                    )
                                }
                            }
                        } else {
                            items(favoriteFoods) { food ->
                                FoodCard(
                                    food = food,
                                    isFavorite = true,
                                    onFavoriteClick = {
                                        onFavoriteClick(food)
                                    },
                                    onAddToCart = {
                                        cartViewModel.addToCart(food)
                                    }
                                )
                            }
                        }
                    }
                    // ORDERS TAB
                    "Orders" -> {
                        item {
                            MyOrdersScreen(
                                orderViewModel = orderViewModel
                            )
                        }
                    }
                    // PROFILE TAB
                    "Profile" -> {
                        item {
                            Text(
                                text = "My Profile",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Your account details will appear here.",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}