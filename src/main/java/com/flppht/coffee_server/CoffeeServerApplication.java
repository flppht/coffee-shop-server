package com.flppht.coffee_server;

import com.flppht.coffee_server.model.Barista;
import com.flppht.coffee_server.model.Barman;
import com.flppht.coffee_server.model.Coffee;
import com.flppht.coffee_server.repository.BaristaRepository;
import com.flppht.coffee_server.repository.BarmanRepository;
import com.flppht.coffee_server.repository.CoffeeRepository;
import com.flppht.coffee_server.repository.CoffeeOrderRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoffeeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeServerApplication.class, args);
	}

	@Bean
	public ApplicationRunner initializer(CoffeeRepository coffeeRepository, BarmanRepository barmanRepository, BaristaRepository baristaRepository, CoffeeOrderRepository coffeeOrderRepository) {
		return args -> {
			//add initial coffee types to the db
			Coffee espresso = coffeeRepository.save(new Coffee("espresso", 1, "https://images.unsplash.com/photo-1508088405209-fbd63b6a4f50", 7, 35));
			Coffee espressoDoppio =coffeeRepository.save(new Coffee("espresso doppio", 2, "https://images.unsplash.com/photo-1610889556528-9a770e32642f", 14, 45));
			Coffee cappuccino =coffeeRepository.save(new Coffee("cappuccino", 2.5, "https://images.unsplash.com/photo-1620052087057-bfd8235f5874", 7, 60));

			//add a barman
			barmanRepository.save(new Barman());

			// add 3 baristas
			baristaRepository.save(new Barista());
			baristaRepository.save(new Barista());
			baristaRepository.save(new Barista());

		};
	}
}
