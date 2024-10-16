package dev.isaiflores.myproject.demo;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileWriter;
import java.io.IOException;

@Controller
public class NumberController {

    private static final String CSV_FILE_PATH = "numbers.csv";


    @GetMapping("/numberForm")
    public String showForm(Model model){
        model.addAttribute("numberModel",new NumberModel());
        return "numberForm";
    }

    @PostMapping("/check")
    public String checkNumber(@RequestParam("number")String number, Model model){
        String result = "";
        String error = "";
        NumberModel numberModel = new NumberModel();


        try {
            if (number == null || number.trim().isEmpty()) {
                error = "Input cannot be empty.";
                throw new IllegalArgumentException("Empty input");
            }

            if (!number.matches("\\d+")) {
                error = "Please enter a valid number.";
                throw new NumberFormatException("Invalid number format");
            }

            int num = Integer.parseInt(number);
            numberModel.setNumber(num); // Assuming your model has a setNumber method

            // Check if the number is odd or even
            result = (num % 2 == 0) ? "The number is even." : "The number is odd.";

        } catch (NumberFormatException e) {
            error = "Please enter a valid number.";
        } catch (IllegalArgumentException e) {
            error = "Input cannot be empty.";
        }


        writeToCSV(numberModel, error.isEmpty() ? null : error);


        model.addAttribute("result",result);
        model.addAttribute("error", error);
        model.addAttribute("numberModel",numberModel);
        return "numberForm";
    }

    private void writeToCSV(NumberModel numberModel, String error){
        try(CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH,true))){
            String[] entries = new String[2];
            entries[0] = (numberModel != null) ? String.valueOf(numberModel.getNumber()) : "N/A";
            entries[1] = (error != null)? error: "No Error";
            writer.writeNext(entries);
        }catch(IOException e){
            e.printStackTrace();
        }
    }






}
