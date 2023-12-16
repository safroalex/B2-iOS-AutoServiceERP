//
//  AuthNetworkManager.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 16.12.2023.
//

import Foundation

class AuthNetworkManager {
    static let shared = AuthNetworkManager()

    func login(username: String, password: String, completion: @escaping (Bool, String?) -> Void) {
        guard let url = URL(string: "http://localhost:8080/api/auth/login") else {
            completion(false, "Некорректный URL")
            return
        }

        let loginRequest = LoginRequest(username: username, password: password)
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            let jsonData = try JSONEncoder().encode(loginRequest)
            request.httpBody = jsonData
        } catch {
            completion(false, "Ошибка кодирования данных")
            return
        }

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                completion(false, "Ошибка сети: \(error.localizedDescription)")
                return
            }

            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                completion(false, "Ошибка сервера или неверные данные")
                return
            }

            completion(true, nil)
        }.resume()
    }
}
