//
//  ServiceListView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 26.11.2023.
//

import SwiftUI

struct ServiceListView: View {
    // Здесь может быть переменная состояния для хранения списка услуг
    // Например, @State private var services: [Service] = []

    var body: some View {
        List {
            // Здесь будут отображаться услуги
            // Например, так:
            // ForEach(services, id: \.id) { service in
            //     Text(service.name)
            // }
            // Пока что просто покажем заглушку
            Text("Услуга 1")
            Text("Услуга 2")
            Text("Услуга 3")
        }
        .navigationTitle("Услуги")
        // Здесь можно добавить кнопки или другие элементы управления
    }

    // Здесь могут быть функции для работы с данными, например, загрузка списка услуг
}

struct ServiceListView_Previews: PreviewProvider {
    static var previews: some View {
        ServiceListView()
    }
}

