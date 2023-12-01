//
//  AddServiceView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 01.12.2023.
//

import SwiftUI

struct AddServiceView: View {
    @Binding var services: [Service]
    @State private var name = ""
    @State private var costOur = ""
    @State private var costForeign = ""
    @Environment(\.presentationMode) var presentationMode

    var body: some View {
        NavigationView {
            Form {
                TextField("Название услуги", text: $name)
                TextField("Стоимость для отечественных", text: $costOur)
                    .keyboardType(.decimalPad)
                TextField("Стоимость для импортных", text: $costForeign)
                    .keyboardType(.decimalPad)
                Button("Добавить") {
                    addService()
                }
            }
            .navigationTitle("Новая услуга")
        }
    }

    func addService() {
        guard let costOurDouble = Double(costOur), let costForeignDouble = Double(costForeign) else {
            print("Ошибка ввода стоимости")
            return
        }

        let newService = Service(name: name, costOur: costOurDouble, costForeign: costForeignDouble)
        ServiceNetworkManager.shared.addService(service: newService) { success in
            if success {
                services.append(newService)
                presentationMode.wrappedValue.dismiss()
            } else {
                // Обработка ошибок
            }
        }
    }
}

