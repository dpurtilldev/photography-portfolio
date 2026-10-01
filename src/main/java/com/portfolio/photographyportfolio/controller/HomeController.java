package com.portfolio.photographyportfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Photography Portfolio API</title>
                    <style>
                        body { 
                            font-family: Arial, sans-serif; 
                            max-width: 800px; 
                            margin: 50px auto; 
                            padding: 20px;
                            background-color: #f5f5f5;
                        }
                        h1 { color: #333; }
                        .endpoint { 
                            background: white; 
                            padding: 15px; 
                            margin: 10px 0; 
                            border-radius: 5px;
                            border-left: 4px solid #4CAF50;
                        }
                        .method { 
                            display: inline-block;
                            padding: 3px 8px;
                            background: #4CAF50;
                            color: white;
                            border-radius: 3px;
                            font-size: 12px;
                            font-weight: bold;
                            margin-right: 10px;
                        }
                        .method.post { background: #2196F3; }
                        .method.put { background: #FF9800; }
                        .method.delete { background: #f44336; }
                        code { 
                            background: #f4f4f4; 
                            padding: 2px 6px; 
                            border-radius: 3px;
                            font-family: 'Courier New', monospace;
                        }
                        .status { 
                            color: #4CAF50; 
                            font-weight: bold;
                        }
                        a { 
                            color: #2196F3; 
                            text-decoration: none;
                        }
                        a:hover { text-decoration: underline; }
                    </style>
                </head>
                <body>
                    <h1>📷 Photography Portfolio API</h1>
                    <p class="status">✅ API is running successfully!</p>
                    
                    <h2>Available Endpoints:</h2>
                    
                    <div class="endpoint">
                        <span class="method">GET</span>
                        <a href="/api/photos/health" target="_blank">/api/photos/health</a>
                        - Health check
                    </div>
                    
                    <div class="endpoint">
                        <span class="method">GET</span>
                        <a href="/api/photos" target="_blank">/api/photos</a>
                        - Get all photos
                    </div>
                    
                    <div class="endpoint">
                        <span class="method">GET</span>
                        <code>/api/photos/{id}</code>
                        - Get photo by ID
                    </div>
                    
                    <div class="endpoint">
                        <span class="method post">POST</span>
                        <code>/api/photos</code>
                        - Create new photo
                    </div>
                    
                    <div class="endpoint">
                        <span class="method put">PUT</span>
                        <code>/api/photos/{id}</code>
                        - Update photo
                    </div>
                    
                    <div class="endpoint">
                        <span class="method delete">DELETE</span>
                        <code>/api/photos/{id}</code>
                        - Delete photo
                    </div>
                    
                    <h3>Search & Filter:</h3>
                    
                    <div class="endpoint">
                        <span class="method">GET</span>
                        <code>/api/photos/search?q={term}</code>
                        - Search photos
                    </div>
                    
                    <div class="endpoint">
                        <span class="method">GET</span>
                        <code>/api/photos/tag/{tag}</code>
                        - Get photos by tag
                    </div>
                    
                    <div class="endpoint">
                        <span class="method">GET</span>
                        <a href="/api/photos/public" target="_blank">/api/photos/public</a>
                        - Get public photos
                    </div>
                    
                    <div class="endpoint">
                        <span class="method">GET</span>
                        <a href="/api/photos/recent" target="_blank">/api/photos/recent</a>
                        - Get recent uploads
                    </div>
                    
                    <div class="endpoint">
                        <span class="method">GET</span>
                        <code>/api/photos/location/{location}</code>
                        - Get photos by location
                    </div>
                    
                    <div class="endpoint">
                        <span class="method">GET</span>
                        <code>/api/photos/camera/{model}</code>
                        - Get photos by camera
                    </div>
                    
                    <h3>⚠️ Note:</h3>
                    <p>MongoDB is not connected. Install and start MongoDB to use database features.</p>
                    <p>Documentation: <a href="https://www.mongodb.com/try/download/community" target="_blank">Download MongoDB</a></p>
                </body>
                </html>
                """;
    }
}
