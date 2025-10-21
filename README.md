# Prueba Tecnica Helipagos - Android

Este repositorio es parte de una Prueba Tecnica para la postulacion del puesto de Desarrollador Mobile en Helipagos,
La aplicacion consume la API de helipagos en entorno Sandbox, para manejar solicitudes de pagos.
### Caracteristicas basicas:
- Listar solicitudes de pagos
- Ver detalle de solicitud
- Crear solicitud de pago

La aplicación permite gestionar solicitudes de pago dentro del ecosistema Helipagos. Los usuarios pueden visualizar pagos existentes, consultar el detalle de cada solicitud y crear nuevas órdenes de cobro consumiendo la API oficial en entorno Sandbox. Su propósito es demostrar buenas prácticas de arquitectura moderna en Android, uso de Jetpack Compose y asincronía con Coroutines/Flow, entre otros.

---

## Tecnologías Utilizadas

### Lenguaje y Framework
- **Kotlin** `2.0.21`
- **Jetpack Compose** `ComposeBom 2024-09.00`
- **Compose Material3**

### Inyección de Dependencias
- **Hilt** `2.51.1`

### Networking
- **Retrofit** `2.11.0`
- **OkHttp** `4.12.0`
- **Kotlinx Serialization** `1.7.1`

### Paginación
- **Paging 3** `3.3.0`

### Asincronía
- **Kotlin Coroutines** `1.8.1`
- **Flow** 

### Testing
- **JUnit 4** `4.13.2`
- **Mockk** `1.13.11`
- **Coroutines Test** `1.8.1`
- **Turbine** `1.1.0`

### Herramientas de Desarrollo
- **Android Studio** Narwhal 4 Feature Drop (2025.1.4) o superior
- **Gradle** `8.13.0`
- **Kotlin DSL**

### Navegación
- **Navigation Compose** `2.7.7`

### Dependencias Principales
```kotlin
dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Retrofit & Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Navigation
    implementation(libs.navigation.compose)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

    // Paging 3
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}
```

---

## Arquitectura

La aplicación implementa **MVVM (Model-View-ViewModel)** con **Clean Architecture**:

```
┌─────────────────────────────────────────────────┐
│                 Presentation(ui)                │
│  (UI Layer - Compose, ViewModels, UIState)      │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│                  Domain                         │
│     (Use Cases, Repository Interfaz, Models)          │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│                   Data                          │
│  (Repository Impl, API Service, DTOs, Paging, Mapper)   │
└─────────────────────────────────────────────────┘
```

### Capas

#### 1. **Presentation Layer** (`ui/`)
- **Screens**: Componentes Compose que representan pantallas completas
- **ViewModels**: Lógica de presentación y manejo de estado
- **UIState**: Data classes que representan el estado de la UI

#### 2. **Domain Layer** (`domain/`)
- **Use Cases**: Casos de uso con lógica de negocio
- **Models**: Modelos de dominio puros (sin dependencias de Android)
- **Repository Interfaces**: Contratos que define la capa de datos

#### 3. **Data Layer** (`data/`)
- **Repository Implementations**: Implementación de repositorios
- **Remote Data Source**: API calls con Retrofit
- **DTOs**: Data Transfer Objects para la API
- **Paging**: Paginación
- **Mapper**: Mapeo de DTOs -> Domain

### Flujo de Datos
```
User Action → UI (Compose) → ViewModel → Use Case → Repository → API
API → Repository → Use Case → ViewModel → UIState → UI
```

### Manejo de Estado
- **StateFlow**: Para estados de UI reactivos
- **SharedFlow**: Para eventos one-shot
- **Paging Data**: Para listas paginadas

---

## Requisitos Previos

### Software Necesario
- **Android Studio** Narwhal 4 Feature Drop (2025.1.4) o superior
- **Android SDK**:
  - Compile SDK: 36
  - Min SDK: 24 (Android 7.0)
  - Target SDK: 36
- **Git** para clonar el repositorio

### Configuración del Entorno
1. Instalar Android Studio desde [developer.android.com](https://developer.android.com/studio)
2. Configurar Android SDK y emulador (o dispositivo físico)
3. Habilitar modo desarrollador en dispositivo físico (opcional)

---

## Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone https://github.com/Pedro-kt/helipagos-android.git
cd tu-proyecto-android
```

### 2. Abrir en Android Studio
1. Abrir Android Studio
2. Seleccionar `File > Open`
3. Navegar al directorio del proyecto
4. Esperar a que Gradle sincronice las dependencias

### 3. Configurar Variables de Entorno

#### Opción A: Usando `local.properties`
Crear archivo `local.properties` en la raíz del proyecto:
```properties
sdk.dir=/path/to/android/sdk
BASE_URL=https://api.ejemplo.com/v1/
API_KEY=tu_api_key
```

#### Opción B: Usando `gradle.properties`
Agregar en `gradle.properties`:
```properties
BASE_URL=https://api.ejemplo.com/v1/
API_KEY=tu_api_key
```

#### Opción C: Usando BuildConfig
El proyecto ya tiene configurado BuildConfig en `build.gradle.kts`:

```kotlin
// app/build.gradle.kts
android {
    defaultConfig {
        // ...
        buildConfigField("String", "BASE_URL", "\"https://api.ejemplo.com/v1/\"")
        buildConfigField("String", "API_KEY", "\"API_KEY\"")
    }
}
```

Uso en código:
```kotlin
@Provides
@Singleton
fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory())
        .build()
}
```

### 4. Sincronizar Gradle
```bash
./gradlew clean build
```

### 5. Ejecutar la Aplicación
1. Conectar dispositivo Android o iniciar emulador
2. Presionar el botón (Run) en Android Studio
3. O ejecutar desde terminal:
```bash
./gradlew installDebug
```

---

## Requisitos Implementados

### Requisitos Principales logrados

- [x] **1**: Lista de solicitudes de pago
  - Consumo del endpoint correspondiente con Retrofit
  - Screen de listas de solicitudes utilizando LazyColumn y Paging 3 con scroll infinito
  - Indicador de carga 
  - Manejo de estados (loading, success, error) + retry
  - PullToRefreshBox
  
- [x] **2**: Detalle de solicitud
  - Consumo del endpoint correspondiente con Retrofit
  - Navegación desde lista a detalle
  - Carga de información completa

- [x] **3**: Crear solicitud de pago
  - Consumo del endpoint correspondiente con Retrofit
  - FAB en la vista principal de listado que navega a esta screen
  - Formulario con validaciones reactivas:
    - Uso de onValueChange en TextFields que actualiza un MutableStateOf
    - UX con feedback, y errores.
    - Importe:
      - Obligatorio
      - Mayor a $100
    - Descripción:
      - Obligatoria
      - Mínimo 5 caracteres
    - Referencia externa:
      - Obligatoria
      - Debe ser única
    - Fecha de vencimiento:
      - Opcional
      - Por defecto: 2025-12-31
    - **Submit button:**
      - `enabled = isValidForm`


### Consideraciones tecnicas logrados

- [x] **Arquitectura**: features/paymentrequests con capas internas (ui, domain. data), con usecases y repository
- [x] **Network**: Retrofit + OkHttp, Kotlin Serialization, Interceptor auth y logging
- [x] **Compose specifics**: UI con Material3, uso de remember y LaunchedEffect, soporte para modo oscuro
- [x] **Concurrency**: Kotlin Coroutines + Flow, integracion de flatMapLatest en `CreatePaymentViewModel`lo que cancela automáticamente una solicitud anterior si llega una nueva antes de terminar, uso de viewModelScope para operaciones UI, y uso de stateFlow / mutableStateFLow para observacion del estado por compose
- [x] **DI**: Hilt para inyectar dependencias
- [x] **Pagination**: Uso de Paging 3 sin RemoteMediator, se utiliza a modo de scroll infinito 
- [x] **State Management**: Utilizacion de UiState usando sealed class (loading, data, error) y se expone con StateFlow<UiState> desde el ViewModel
- [x] **Testing**: Alcance a Tests Unitarios (Unit Test) para ViewModels, RepositoryImpl, y paging, NO cuenta con Test de UI (UI tests)

---

## Funcionalidades Bonus

### Features Bonus Implementados

- [x] **BONUS-001**: Validaciones avanzadas en el formulario de Crear Solicitud de pago, con animaciones Reactivas
  - Muestra feedback y errores en campos
  - Deshabilita el boton de crear si no es valido el formulario
  - Uso de State y derivedStateOf para validez de formulario
  
- [x] **BONUS-002**: Pull-to-refresh
  
- [x] **BONUS-003**: Paging 3
  - Integracion de Paging 3 y Retrofit para paginación sin persistencia local

### Features Ultra-Bonus Implementados
  
- [x] **ULTRA-001**: Tema dinámico (Material You)
  - Colores adaptativos del sistema
  - Dynamic theming en Android 12+

- [x] **ULTRA-002**: uso de flatMapLatest para cancelacion de Flow en CreatePaymentViewModel

### Otras Mejoras

- README completo y detallado

---

## Testing

### Estructura de Tests

```
app/
├── src/
│   ├── test/                  # Unit tests
│   │   ├── viewmodel/
│   │   ├── paging/
│   │   └── repository/
```

### Ejecutar Tests

#### Tests Unitarios (JVM)
```bash
# Ejecutar todos los tests unitarios
./gradlew test

# Solo tests de debug
./gradlew testDebugUnitTest

Tests con Coverage (JaCoCo)

# Primero configura JaCoCo en build.gradle.kts (ver documentación)
# Luego ejecuta:
./gradlew testDebugUnitTest jacocoTestReport

# Reporte HTML en: app/build/reports/jacoco/jacocoTestReport/html/index.html
```

---

## 📱 Generación de APK

### APK Debug (para desarrollo)

```bash
./gradlew assembleDebug
```

El APK se genera en: `app/build/outputs/apk/debug/app-debug.apk`

### APK Release (firmado)

#### 1. Configurar Keystore

Crear `keystore.properties` en la raíz del proyecto:
```properties
storePassword=tu_store_password
keyPassword=tu_key_password
keyAlias=tu_key_alias
storeFile=path/to/keystore.jks
```

#### 2. Configurar build.gradle.kts

```kotlin
// app/build.gradle.kts
android {
    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

#### 3. Generar APK Release

```bash
./gradlew assembleRelease
```

El APK se genera en: `app/build/outputs/apk/release/app-release.apk`

### Android App Bundle (AAB)

El AAB se genera en: `app/build/outputs/bundle/release/app-release.aab`

### Instalación Manual

```bash
# Instalar APK debug
adb install app/build/outputs/apk/debug/app-debug.apk

# Instalar APK release
adb install app/build/outputs/apk/release/app-release.apk
```

### Descargar APK

En la sección de Releases del repositorio se encuentra:
- APK debug para testing
- APK release firmado (última versión)

---

## Estructura del Proyecto

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/pedro/helipagospayment/
│   │   │   ├── di/                    # Injeccion de dependencias
│   │   │   │   ├── RepositoryModule.kt
│   │   │   │   └── NetworkModule.kt
│   │   │   │
│   │   │   ├── common/                #Componentes reutilizables
│   │   │   │   └── ui/
│   │   │   │       └── components/
│   │   │   │           ├── AppScaffold.kt
│   │   │   │           ├── ErrorView.kt
│   │   │   │           ├── Loader.kt
│   │   │   │           ├── PaymentDetailCard.kt
│   │   │   │           ├── PaymentListCard.kt
│   │   │   │           └── TopAppBarComponent.kt
│   │   │   │
│   │   │   ├── core/                             # core
│   │   │   │   └── network/
│   │   │   │       └── AuthInterceptor.kt
│   │   │   │
│   │   │   │
│   │   │   ├── features/
│   │   │   │   └── paymentrequests/
│   │   │   │       │
│   │   │   │       ├── data/                  # Data Layer
│   │   │   │       │   ├── api/
│   │   │   │       │   │   └── PaymentApi.kt
│   │   │   │       │   ├── model/
│   │   │   │       │   │   ├── CreatePaymentRequestDto.kt
│   │   │   │       │   │   ├── CreatePaymentResponseDtoo.kt
│   │   │   │       │   │   ├── PaymentResponseDto.kt
│   │   │   │       │   │   ├── PaymentPagedDto.kt
│   │   │   │       │   │   └── PaymentsPagedResponseDto.kt
│   │   │   │       │   ├── paging/
│   │   │   │       │   │   └── PaymentPagingSource.kt
│   │   │   │       │   ├── repository/
│   │   │   │       │   │    └── PaymentRepositoryImpl.kt
│   │   │   │       │   └── mapper/
│   │   │   │       │        ├── PagedDtoToPagedModel.kt
│   │   │   │       │        └── PaymentDtoToPaymentModel.kt
│   │   │   │       │
│   │   │   │       ├── domain/                  # Domain Layer
│   │   │   │       │   ├── usecases/
│   │   │   │       │   │   ├── CreatePaymentUseCase.kt
│   │   │   │       │   │   ├── GetPaymentDetailUseCase.kt
│   │   │   │       │   │   └── GetPaymentsPagedUseCase.kt
│   │   │   │       │   ├── repository/
│   │   │   │       │   │    └── PaymentRepository.kt
│   │   │   │       │   └── model/
│   │   │   │       │        ├── PaynentPaged.kt
│   │   │   │       │        └── PaymentResponse.kt
│   │   │   │       │
│   │   │   │       └── ui/                  # Presentation Layer
│   │   │   │           ├── create/
│   │   │   │           │   ├── CreatePaymentScreen.kt
│   │   │   │           │   ├── CreatePaymentUiState.kt
│   │   │   │           │   └── CreatePaymentViewModel.kt
│   │   │   │           ├── detail/
│   │   │   │           │   ├── PaymentDetailScreen.kt
│   │   │   │           │   ├── PaymentDetailUiState.kt
│   │   │   │           │   └── PayemntDetailViewModel.kt
│   │   │   │           └── list/
│   │   │   │               ├── PaymentRequestsScreen.kt
│   │   │   │               └── PaymentRequestsViewModel.kt
│   │   │   │  
│   │   │   ├── navigation/                  # Navegación
│   │   │   │   ├── AppNavigation.kt
│   │   │   │   └── Destinations-kt
│   │   │   │  
│   │   │   ├── ui/
│   │   │   │   └── theme/
│   │   │   │       ├── Color.kt
│   │   │   │       ├── Theme.kt
│   │   │   │       └── Type.kt
│   │   │   │
│   │   │   ├── HelipagosPaymentApp.kt
│   │   │   └── MainActivity.kt
│   │
│   └── test/                          # Unit tests
│       └── java/com/pedro/helipagospayment/
│           ├── paging/
│           │   └── PaymentPagingSourceTest-kt     # Test unitario de PaymentPagingSource
│           ├── viewmodel/
│           │   ├── CreatePaymentViewModelTest.kt
│           │   ├── PaymentDetailViewModelTest.kt
│           │   └── PaymentRequestsViewModelTestkt
│           └── repository/
│               └── PaymentRepositoryImpl           # Test unitario de la implementacion de PaymentRepository
│   
│
├── build.gradle.kts
├── proguard-rules.pro
└── gradle.properties
```

---

## Decisiones de Diseño

### Arquitectura
**Decisión**: Usar MVVM + Clean Architecture
**Razón**: 
- Separación clara de responsabilidades
- Facilita testing independiente de cada capa
- Escalabilidad y mantenibilidad del código
- Independencia de frameworks en la capa de dominio

### Inyección de Dependencias
**Decisión**: Hilt en lugar de Koin
**Razón**:
- Recomendado oficialmente por Google
- Mejor integración con AndroidX
- Performance óptima

### Paginación
**Decisión**: Paging 3 Library
**Razón**:
- Integración nativa con Compose
- Manejo automático de estados
- Cache y sincronización eficiente
- Soporte para múltiples fuentes de datos

### Networking
**Decisión**: Retrofit + OkHttp
**Razón**:
- Estándar de la industria
- Type-safe por diseño
- Interceptores para logging y autenticación
- Excelente integración con Coroutines

### Testing
**Decisión**: JUnit + Mockk + Turbine
**Razón**:
- Mockk diseñado para Kotlin
- Turbine simplifica testing de Flows
- Stack completo y maduro

## Desafíos y Aprendizajes

- Integración de Paging 3 con API.
- Cancelación de llamadas concurrentes usando `flatMapLatest` en CreatePaymentViewModel.
- Implementación de tests unitarios.

## Futuras Mejoras que se pueden implementar

- Tests de UI con `ComposeTestRule`
- Persistencia local con Room + RemoteMediator
- Cancelación manual de solicitudes de pago
- Internacionalización (i18n)
- Offline-first architecture

## Autor

Desarrollado por **Pedro Bustamante**  
- Contacto: bustamante.dev.mobile@gmail.com
- GitHub: https://github.com/Pedro-kt
