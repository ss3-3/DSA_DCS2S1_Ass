package Airline;

public class InputValidator {
    
    public static String validateAndFormatCityName(String input) {
        if (input == null) return null;
        
        // Trim whitespace
        String cleaned = input.trim();
        if (cleaned.isEmpty()) return null;
        
        // Convert to title case (capitalize first letter of each word)
        String[] words = cleaned.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > 0) {
                // Capitalize first letter of each word
                result.append(words[i].substring(0, 1).toUpperCase());
                if (words[i].length() > 1) {
                    result.append(words[i].substring(1));
                }
                
                // Add space between words (except for last word)
                if (i < words.length - 1) {
                    result.append(" ");
                }
            }
        }
        
        return result.toString();
    }
    
    public static boolean isValidCityName(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return false;
        }
        
        // Check if contains only letters and spaces
        String cleaned = cityName.trim();
        return cleaned.matches("[a-zA-Z\\s]+") && cleaned.length() >= 2;
    }
    
    public static boolean isValidChoice(String choice, int min, int max) {
        try {
            int num = Integer.parseInt(choice.trim());
            return num >= min && num <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}