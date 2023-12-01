//
//  CarNetworkManager.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 01.12.2023.
//

import Foundation

class CarNetworkManager {
    static let shared = CarNetworkManager()

    private init() {}

    func addCar(car: Car, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/cars") else {
            print("Invalid URL")
            completion(false)
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            let jsonData = try JSONEncoder().encode(car)
            request.httpBody = jsonData
        } catch {
            print("Failed to encode car")
            completion(false)
            return
        }

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion(false)
                return
            }

            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                print("Invalid response from server")
                completion(false)
                return
            }

            completion(true)
        }.resume()
    }

    func fetchAllCars(completion: @escaping ([Car]) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/cars") else {
            print("Invalid URL")
            completion([])
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "GET"

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion([])
                return
            }

            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                print("Invalid response from server")
                completion([])
                return
            }

            guard let data = data else {
                print("No data received from server")
                completion([])
                return
            }

            do {
                let cars = try JSONDecoder().decode([Car].self, from: data)
                completion(cars)
            } catch {
                print("Error decoding JSON: \(error)")
                completion([])
            }
        }.resume()
    }

    func updateCar(carId: Int, updatedCar: Car, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/cars/\(carId)") else {
            print("Invalid URL")
            completion(false)
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "PUT"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            let jsonData = try JSONEncoder().encode(updatedCar)
            request.httpBody = jsonData
        } catch {
            print("Failed to encode car")
            completion(false)
            return
        }

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion(false)
                return
            }

            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                print("Invalid response from server")
                completion(false)
                return
            }

            completion(true)
        }.resume()
    }
    
    func deleteCar(carId: Int, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/cars/\(carId)") else {
            print("Invalid URL")
            completion(false)
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "DELETE"

        URLSession.shared.dataTask(with: request) { _, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion(false)
                return
            }

            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                print("Invalid response from server")
                completion(false)
                return
            }

            completion(true)
        }.resume()
    }

}
