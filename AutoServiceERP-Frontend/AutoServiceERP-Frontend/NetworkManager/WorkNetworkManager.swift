//
//  WorkNetworkManager.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 02.12.2023.
//

import Foundation

class WorkNetworkManager {
    static let shared = WorkNetworkManager()

    private init() {}

    func addWork(work: Work, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/works") else {
            print("Invalid URL")
            completion(false)
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            let encoder = JSONEncoder()
            encoder.dateEncodingStrategy = .iso8601 // Настройка формата даты
            let jsonData = try encoder.encode(work)
            print("Serialized JSON: \(String(data: jsonData, encoding: .utf8) ?? "")") // Отладочный вывод
            request.httpBody = jsonData
        } catch {
            print("Failed to encode work")
            completion(false)
            return
        }

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error making request: \(error.localizedDescription)")
                completion(false)
                return
            }

            if let httpResponse = response as? HTTPURLResponse {
                print("HTTP Status Code: \(httpResponse.statusCode)")
                if let data = data, let responseString = String(data: data, encoding: .utf8) {
                    print("Response: \(responseString)")
                }
            }
            
            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                print("Invalid response from server")
                completion(false)
                return
            }

            completion(true)
        }.resume()
    }

    // Методы для получения, обновления и удаления работы будут добавлены аналогично
}
