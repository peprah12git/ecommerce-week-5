# SmartCommerce Frontend

A React application for the SmartCommerce e-commerce platform with both client-facing and admin interfaces.

## Getting Started

### Prerequisites
- Node.js 18+ 
- npm or yarn

### Installation

```bash
cd frontend
npm install
```

### Running the Application

```bash
npm start
```

The application will run on `http://localhost:3000`.

Make sure the Spring Boot backend is running on `http://localhost:8082`.

## Project Structure

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/          # Reusable UI components
│   │   ├── Header/
│   │   ├── Loading/
│   │   ├── Modal/
│   │   ├── Pagination/
│   │   └── ProductCard/
│   ├── context/             # React Context for state management
│   │   └── AppContext.js
│   ├── pages/
│   │   ├── admin/           # Admin panel pages
│   │   │   ├── AdminLayout/
│   │   │   ├── Categories/
│   │   │   ├── Dashboard/
│   │   │   └── Products/
│   │   └── client/          # Customer-facing pages
│   │       ├── Categories/
│   │       ├── Home/
│   │       ├── ProductDetail/
│   │       ├── Products/
│   │       └── Register/
│   ├── services/            # API service layer
│   │   ├── api.js
│   │   ├── categoryService.js
│   │   ├── productService.js
│   │   └── userService.js
│   ├── App.js
│   ├── index.css
│   └── index.js
└── package.json
```

## Features

### Client App
- **Home Page**: Featured products and categories
- **Products**: Browse, search, filter, and paginate products
- **Product Detail**: View product information
- **Categories**: Browse all categories
- **User Registration**: Create new account

### Admin Panel
- **Dashboard**: Overview with stats and quick actions
- **Product Management**: CRUD operations for products
- **Category Management**: CRUD operations for categories

## API Configuration

The API base URL can be configured via environment variable:

```bash
REACT_APP_API_URL=http://localhost:8082/api
```

## Technologies Used
- React 18
- React Router v6
- Axios for HTTP requests
- Lucide React for icons
- CSS (no framework)
